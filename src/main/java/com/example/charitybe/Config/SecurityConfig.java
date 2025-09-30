package com.example.charitybe.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
//                .csrf(csrf -> csrf.disable()) // tắt CSRF cho API
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/**",
//                                "/swagger-ui/index.html",
//                                "/api/v1/**"
//                        ).permitAll() // cho phép proxy
//                        .anyRequest().authenticated()          // các route khác phải login
//                );



        return http.build();
    }
}
