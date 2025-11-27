package com.example.charitybe.Config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    // Lấy giá trị từ application.properties
    @Value("${app.rate-limit.permits-per-second:5.0}")
    private double permitsPerSecond;

    /**
     * Tạo Bean RateLimiter.
     * RateLimiter là thread-safe và được chia sẻ toàn cục.
     */
    @Bean
    public RateLimiter apiRateLimiter() {
        // Tạo RateLimiter với số lượng requests/giây được phép
        return RateLimiter.create(permitsPerSecond);
    }
}