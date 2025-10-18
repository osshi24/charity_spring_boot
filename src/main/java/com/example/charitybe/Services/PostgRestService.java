package com.example.charitybe.Services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.Enumeration;

@Service
public class PostgRestService {

    private final RestTemplate restTemplate;

    @Value("${spring.application.server_postgrest}")
    private String postgrestUrl;

    public PostgRestService() {
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    public ResponseEntity<String> forwardRequest(String method, String path,
                                                 String queryString, String body,
                                                 HttpServletRequest request) {
        try {
            // Xây dựng URL
            String url = postgrestUrl + path + (queryString != null ? "?" + queryString : "");

            // Copy headers
            HttpHeaders headers = copyHeaders(request);
            if ("POST".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
                headers.add("Prefer", "return=representation");
            }

            // Gọi PostgREST
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.valueOf(method), new HttpEntity<>(body, headers), String.class
            );

            // Xóa CORS headers
            HttpHeaders clean = new HttpHeaders();
            response.getHeaders().forEach((k, v) -> {
                if (!k.toLowerCase().startsWith("access-control")) clean.put(k, v);
            });

            return ResponseEntity.status(response.getStatusCode()).headers(clean).body(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private HttpHeaders copyHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String lower = name.toLowerCase();
            if (!lower.equals("host") && !lower.equals("content-length") && !lower.startsWith("x-forwarded")) {
                headers.add(name, request.getHeader(name));
            }
        }
        return headers;
    }
}