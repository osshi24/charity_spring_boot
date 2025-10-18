// package com.example.charitybe.Services;

// import jakarta.servlet.http.HttpServletRequest;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.client.HttpStatusCodeException;
// import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;


// import java.util.Enumeration;

// @Service
// public class PostgRestService {
//     private final RestTemplate restTemplate;

//     @Value("${spring.application.server_postgrest}")
//     private String postgrestUrl;

//     public PostgRestService() {
//         // Dùng HttpComponentsClientHttpRequestFactory để hỗ trợ PATCH
//         this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
//     }

//     public ResponseEntity<String> forwardRequest(String method, String path,
//                                                  String queryString, String body,
//                                                  HttpServletRequest request) {
//         try {
//             // Xây dựng URL
//             StringBuilder urlBuilder = new StringBuilder(postgrestUrl + path);
//             if (queryString != null) {
//                 urlBuilder.append("?").append(queryString);
//             }

//             // Copy headers
//             HttpHeaders headers = new HttpHeaders();
//             Enumeration<String> headerNames = request.getHeaderNames();
//             while (headerNames.hasMoreElements()) {
//                 String headerName = headerNames.nextElement();
//                 if (!skipHeader(headerName)) {
//                     headers.add(headerName, request.getHeader(headerName));
//                 }
//             }

//             // Thêm header để PostgREST trả về dữ liệu bản ghi vừa tạo
//             if ("POST".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
//                 headers.add("Prefer", "return=representation");
//             }

//             // Tạo request entity
//             HttpEntity<String> entity = new HttpEntity<>(body, headers);

//             // Gọi PostgREST
//             return restTemplate.exchange(
//                     urlBuilder.toString(),
//                     HttpMethod.valueOf(method),
//                     entity,
//                     String.class);

//         } catch (HttpStatusCodeException ex) {
//             return ResponseEntity
//                     .status(ex.getStatusCode())
//                     .body(ex.getResponseBodyAsString());
//         } catch (Exception e) {
//             return ResponseEntity.status(500)
//                     .body("{\"error\":\"" + e.getMessage() + "\"}");
//         }
//     }

//     private boolean skipHeader(String headerName) {
//         String lower = headerName.toLowerCase();
//         return lower.equals("host") ||
//                 lower.equals("content-length") ||
//                 lower.startsWith("x-forwarded");
//     }
// }







package com.example.charitybe.Services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.Enumeration;

@Service
public class PostgRestService {
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
            if (queryString != null) {
                urlBuilder.append("?").append(queryString);
            }

            // ============================================================
            // BƯỚC 2: COPY HEADERS TỪ REQUEST GỐC
            // ============================================================
            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                // Bỏ qua một số headers không cần thiết
                if (!skipHeader(headerName)) {
                    headers.add(headerName, request.getHeader(headerName));
                }
            }

            // Thêm header để PostgREST trả về dữ liệu bản ghi vừa tạo/cập nhật
            if ("POST".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
                headers.add("Prefer", "return=representation");
            }

            // ============================================================
            // BƯỚC 3: GỌI POSTGREST
            // ============================================================
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                    urlBuilder.toString(),
                    HttpMethod.valueOf(method),
                    entity,
                    String.class);

            // ============================================================
            // BƯỚC 4: XÓA CORS HEADERS TỪ POSTGREST RESPONSE
            // ============================================================
            // Đây là bước QUAN TRỌNG NHẤT để fix lỗi CORS!
            // PostgREST có thể trả về Access-Control-Allow-Origin: *
            // Nếu không xóa, Spring Boot sẽ thêm header của nó
            // → Kết quả: 2 giá trị CORS → LỖI!
            
            HttpHeaders cleanHeaders = new HttpHeaders();
            cleanHeaders.putAll(response.getHeaders());
            
            // Xóa TẤT CẢ các CORS-related headers từ PostgREST
            cleanHeaders.remove(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
            cleanHeaders.remove(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS);
            cleanHeaders.remove(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS);
            cleanHeaders.remove(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS);
            cleanHeaders.remove(HttpHeaders.ACCESS_CONTROL_MAX_AGE);
            cleanHeaders.remove(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS);
            cleanHeaders.remove("Access-Control-Request-Method");
            cleanHeaders.remove("Access-Control-Request-Headers");

            // ============================================================
            // BƯỚC 5: TRẢ VỀ RESPONSE SẠCH
            // ============================================================
            // Spring Boot CorsConfig.java sẽ TỰ ĐỘNG thêm CORS headers đúng
            // Không cần lo lắng về CORS ở đây nữa
            
            return ResponseEntity
                    .status(response.getStatusCode())
                    .headers(cleanHeaders)  // ✅ Headers đã sạch, không còn CORS từ PostgREST
                    .body(response.getBody());

        } catch (HttpStatusCodeException ex) {
            // Xử lý lỗi HTTP từ PostgREST
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());
                    
        } catch (Exception e) {
            // Xử lý lỗi chung
            return ResponseEntity.status(500)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Kiểm tra xem header có nên được skip không
     * @param headerName Tên header
     * @return true nếu cần skip, false nếu cần forward
     */
    private boolean skipHeader(String headerName) {
        String lower = headerName.toLowerCase();
        return lower.equals("host") ||              // Host sẽ khác khi forward
               lower.equals("content-length") ||     // Content-Length tự động tính
               lower.startsWith("x-forwarded");      // X-Forwarded-* không cần thiết
    }
}