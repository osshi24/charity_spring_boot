package com.example.charitybe.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration 
@Data    
public class VnPayConfig {
@Value("${vnpay.tmn-code}") 
    private String tmnCode;

    @Value("${vnpay.hash-secret}") 
    private String hashSecret;

    @Value("${vnpay.url}")
    private String url;

    @Value("${vnpay.return-url}")
    private String returnUrl;
}