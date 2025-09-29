package com.example.charitybe.Services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Enumeration;

@Service
public class PostgRestService {

    private final RestTemplate restTemplate;
    private final String postgrestUrl = "http://54.251.182.3:3000/"; // URL PostgREST

    public PostgRestService() {
        this.restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> forwardRequest(String method, String path,
            String queryString, String body,
            HttpServletRequest request) {
        try {
            // Xây dựng URL
            StringBuilder urlBuilder = new StringBuilder(postgrestUrl + path);
            if (queryString != null) {
                urlBuilder.append("?").append(queryString);
            }

            // Copy headers
            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (!skipHeader(headerName)) {
                    headers.add(headerName, request.getHeader(headerName));
                }
            }

            // Tạo request entity
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            // Gọi PostgREST
            return restTemplate.exchange(
                    urlBuilder.toString(),
                    HttpMethod.valueOf(method),
                    entity,
                    String.class);

        } catch (HttpStatusCodeException ex) {
            // Trả nguyên status + body từ PostgREST
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());
        } catch (Exception e) {
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