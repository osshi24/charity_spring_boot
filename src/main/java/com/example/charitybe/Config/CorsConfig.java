package com.example.charitybe.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Cho phép credentials (cookies, authorization headers)
        config.setAllowCredentials(true);

        // Cho phép tất cả origins (trong production nên chỉ định cụ thể)
        config.addAllowedOriginPattern("*");
        // Hoặc chỉ định cụ thể:
        // config.addAllowedOrigin("http://localhost:3000");
        // config.addAllowedOrigin("https://yourdomain.com");

        // Cho phép tất cả headers
        config.addAllowedHeader("*");

        // Cho phép tất cả methods (GET, POST, PUT, DELETE, PATCH, OPTIONS)
        config.addAllowedMethod("*");

        // Áp dụng cho tất cả endpoints
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}