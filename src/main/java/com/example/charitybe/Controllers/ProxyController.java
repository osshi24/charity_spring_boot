package com.example.charitybe.Controllers;


import com.example.charitybe.Services.PostgRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class ProxyController {

    @Autowired
    private PostgRestService postgRestService;


    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> proxyRequest(
            HttpServletRequest request,
            @RequestBody(required = false) String body) {

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