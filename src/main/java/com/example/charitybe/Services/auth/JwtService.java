package com.example.charitybe.Services.auth;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessToken.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refreshToken.expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(Long userId) {
        return JWT.create()
                .withSubject(userId.toString())
                // .withClaim("role", roleName)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .sign(Algorithm.HMAC256(secret));
    }

    public String generateRefreshToken(Long userId, String roleName) {
        return JWT.create()
                .withSubject(userId.toString())
                .withClaim("role", roleName)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndGetUserId(String jwt) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(jwt)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}