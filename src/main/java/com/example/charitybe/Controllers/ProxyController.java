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

        String path = request.getRequestURI().substring("/api/v1/".length());
        String queryString = request.getQueryString();
//        truyền header authorization

        System.out.println("request: " + request);

        return postgRestService.forwardRequest(
                request.getMethod(),
                path,
                queryString,
                body,
                request
        );
    }

   
}