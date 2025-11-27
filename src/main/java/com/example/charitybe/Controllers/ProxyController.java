package com.example.charitybe.Controllers;


import com.example.charitybe.Services.AsyncBlockchainProcessor;
import com.example.charitybe.Services.PostgRestPermissionInterceptor;
import com.example.charitybe.Services.PostgRestService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ProxyController {

    @Autowired
    private PostgRestService postgRestService;

    @Autowired
    private PostgRestPermissionInterceptor permissionInterceptor;

    @Autowired(required = false)
    private AsyncBlockchainProcessor blockchainProcessor;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> proxyRequest(
            HttpServletRequest request,
            @RequestBody(required = false) String body) {

        // Get authentication from SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Check permission
        if (!permissionInterceptor.hasPermission(request, auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"error\":\"Insufficient permissions\",\"message\":\"Bạn không có quyền thực hiện hành động này\"}");
        }

        String path = extractForwardPath(request);
        String queryString = request.getQueryString();
        String method = request.getMethod();

        ResponseEntity<String> response = postgRestService.forwardRequest(
                method,
                path,
                queryString,
                body,
                request
        );

        // Trigger blockchain logging for successful POST requests
        if ("POST".equalsIgnoreCase(method) && response.getStatusCode() == HttpStatus.CREATED) {
            triggerBlockchainLogging(path, response.getBody());
        }

        return response;
    }

    /**
     * Trigger blockchain logging for donations and disbursements
     */
    private void triggerBlockchainLogging(String path, String responseBody) {
        if (blockchainProcessor == null || responseBody == null) {
            return;
        }

        try {
            // Parse response to get ID
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // Check if response is an array and get first element
            if (jsonNode.isArray() && jsonNode.size() > 0) {
                jsonNode = jsonNode.get(0);
            }

            // Get ID from response
            if (jsonNode.has("id")) {
                Long id = jsonNode.get("id").asLong();

                // Trigger blockchain logging based on path
                if (path.startsWith("quyen_gop")) {
                    log.info("Triggering blockchain logging for donation {}", id);
                    blockchainProcessor.processDonation(id);

                } else if (path.startsWith("giai_ngan")) {
                    log.info("Triggering blockchain logging for disbursement {}", id);
                    blockchainProcessor.processDisbursement(id);
                }
            }

        } catch (Exception e) {
            log.error("Error triggering blockchain logging: {}", e.getMessage());
        }
    }

    private String extractForwardPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String prefix = contextPath + "/api/v1";
        String requestUri = request.getRequestURI();

        if (!requestUri.startsWith(prefix)) {
            return requestUri.startsWith("/") ? requestUri.substring(1) : requestUri;
        }

        String path = requestUri.substring(prefix.length());
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }

}