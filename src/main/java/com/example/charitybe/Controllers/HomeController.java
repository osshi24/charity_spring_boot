package com.example.charitybe.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HomeController {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.application.server_postgrest}")
    private String postgrestUrl;


    @GetMapping("/")
    public String home() {
        return "Welcome to the Charity API ^^! update cicdssss";
    }

    @GetMapping(value = "/docs", produces = "application/json")
    public ResponseEntity<String> getSpec() throws JsonProcessingException {
        String url = postgrestUrl; // PostgREST root endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/openapi+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        // Parse JSON và thay đổi phần "servers"
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());

        ((ObjectNode) root).putArray("servers")
                .addObject().put("url", postgrestUrl);
        return ResponseEntity.ok(mapper.writeValueAsString(root));
    }
}
