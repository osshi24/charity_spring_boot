package com.example.charitybe.Config;

import org.springframework.security.config.Customizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class Config {

        private final JwtAuthenticationFilter jwtAuthFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(Customizer.withDefaults())
                                .csrf(csrf -> csrf.disable()) // tắt CSRF cho API
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // Add JWT authentication filter
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                                .authorizeHttpRequests(auth -> auth
                                                // Public endpoints - không cần authentication
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                                // Auth endpoints
                                                .requestMatchers("/api/auth/**").permitAll()

                                                // Public read endpoints
                                                .requestMatchers(HttpMethod.GET, "/api/v1/du_an/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/tin_tuc/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/danh_muc_du_an/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/auth/**").permitAll()

                                                // Swagger/Docs
                                                .requestMatchers("/", "/docs", "/swagger/**").permitAll()

                                                // All other requests require authentication
                                                .anyRequest().authenticated()
                                );

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
