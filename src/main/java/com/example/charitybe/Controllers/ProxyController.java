package com.example.charitybe.Controllers;


import com.example.charitybe.Services.PostgRestPermissionInterceptor;
import com.example.charitybe.Services.PostgRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class ProxyController {

    @Autowired
    private PostgRestService postgRestService;

    @Autowired
    private PostgRestPermissionInterceptor permissionInterceptor;


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

        return postgRestService.forwardRequest(
                request.getMethod(),
                path,
                queryString,
                body,
                request
        );
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