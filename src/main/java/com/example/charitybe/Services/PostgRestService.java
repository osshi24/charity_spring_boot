package com.example.charitybe.Services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;

@Service
public class PostgRestService {

    private static final Logger log = LoggerFactory.getLogger(PostgRestService.class);

    private final RestTemplate restTemplate;

    @Value("${spring.application.server_postgrest}")
    private String postgrestUrl;

    public PostgRestService() {
        // Dùng HttpComponentsClientHttpRequestFactory để hỗ trợ PATCH
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    public ResponseEntity<String> forwardRequest(String method, String path,
                                                 String queryString, String body,
                                                 HttpServletRequest request) {
        try {
            // ============================================================
            // BƯỚC 1: XÂY DỰNG URL ĐẾN POSTGREST
            // ============================================================
            StringBuilder urlBuilder = new StringBuilder(postgrestUrl + path);
            if (queryString != null && !queryString.isEmpty()) {
                urlBuilder.append("?").append(queryString);
            }
            String finalUrl = urlBuilder.toString();
            log.debug("➡️ [PostgREST Forward] URL: {}", finalUrl);

            // ============================================================
            // BƯỚC 2: COPY HEADERS TỪ REQUEST GỐC
            // ============================================================
            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> headerNames = request.getHeaderNames();
            log.debug("📦 Incoming Headers:");
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (!skipHeader(headerName)) {
                    String headerValue = request.getHeader(headerName);
                    headers.add(headerName, headerValue);
                    log.debug("   → {}: {}", headerName, headerValue);
                }
            }

            // Thêm header Prefer để PostgREST trả dữ liệu bản ghi mới
            if ("POST".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
                headers.add("Prefer", "return=representation");
                log.debug("✅ Added Prefer header: return=representation");
            }

            // ============================================================
            // BƯỚC 3: GỌI POSTGREST
            // ============================================================
            log.debug("📤 Request Body: {}", body);
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            log.info("🚀 Forwarding [{}] request to PostgREST: {}", method, finalUrl);
            ResponseEntity<String> response = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.valueOf(method),
                    entity,
                    String.class
            );

            // ============================================================
            // BƯỚC 4: DỌN DẸP CORS HEADERS
            // ============================================================
            HttpHeaders cleanHeaders = new HttpHeaders();
            response.getHeaders().forEach((key, value) -> {
                String lower = key.toLowerCase();
                if (!lower.equals("transfer-encoding") &&
                        !lower.equals("content-length")) {
                    cleanHeaders.put(key, value);
                }
            });
            cleanHeaders.putAll(response.getHeaders());
            log.debug("🧹 Cleaning CORS headers from PostgREST response...");

            cleanHeaders.remove(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
            cleanHeaders.remove(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS);
            cleanHeaders.remove(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS);
            cleanHeaders.remove(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS);
            cleanHeaders.remove(HttpHeaders.ACCESS_CONTROL_MAX_AGE);
            cleanHeaders.remove(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS);
            cleanHeaders.remove("Access-Control-Request-Method");
            cleanHeaders.remove("Access-Control-Request-Headers");

            // ============================================================
            // BƯỚC 5: TRẢ RESPONSE SẠCH
            // ============================================================
            log.debug("✅ Response Status: {}", response.getStatusCode());
            log.debug("✅ Response Body: {}", response.getBody());

            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(cleanHeaders)
                    .body(response.getBody());

        } catch (HttpStatusCodeException ex) {
            log.error("❌ HTTP Error from PostgREST [{}]: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("🔥 Exception forwarding request to PostgREST: {}", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private boolean skipHeader(String headerName) {
        String lower = headerName.toLowerCase();
        return lower.equals("host") ||
                lower.equals("content-length") ||
                lower.startsWith("x-forwarded");
    }
}
