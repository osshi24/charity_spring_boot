// package com.example.charitybe.Config;

// import com.example.charitybe.filter.ApiRateLimitingFilter;
// import org.springframework.boot.web.servlet.FilterRegistrationBean;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class FilterConfig {

//     private final ApiRateLimitingFilter apiRateLimitingFilter;

//     public FilterConfig(ApiRateLimitingFilter apiRateLimitingFilter) {
//         this.apiRateLimitingFilter = apiRateLimitingFilter;
//     }

//     @Bean
//     public FilterRegistrationBean<ApiRateLimitingFilter> rateLimitingFilterRegistration() {
//         FilterRegistrationBean<ApiRateLimitingFilter> registration = new FilterRegistrationBean<>();
//         registration.setFilter(apiRateLimitingFilter);

//         // Áp dụng Rate Limit cho TẤT CẢ các đường dẫn API
//         registration.addUrlPatterns("/api/*");

//         // Thiết lập thứ tự ưu tiên (chạy sớm)
//         registration.setOrder(1);
//         return registration;
//     }
// }