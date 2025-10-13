package com.example.charitybe.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // @Bean
    // public WebMvcConfigurer corsConfigurer() {
    //     return new WebMvcConfigurer() {
    //         @Override
    //         public void addCorsMappings(CorsRegistry registry) {
    //             registry.addMapping("/api/v1/**")

    //                     // 1. Chỉ định nguồn gốc được phép (thay đổi theo frontend của bạn)
    //                     // .allowedOrigins("http://localhost:3000")
    //                     .allowedOriginPatterns("*")
    //                     // 2. Chỉ định các phương thức HTTP được phép
    //                     .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

    //                     // 3. Cho phép các header tùy chỉnh (Authorization, Content-Type, v.v.)
    //                     .allowedHeaders("*")

    //                     // 4. Cho phép truyền thông tin xác thực (cookies, authorization headers)
    //                     .allowCredentials(true)

    //                     // 5. Cache kết quả preflight request (giảm request OPTIONS)
    //                     .maxAge(3600);
    //         }
    //     };
    // }

    public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new
    UrlBasedCorsConfigurationSource();
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