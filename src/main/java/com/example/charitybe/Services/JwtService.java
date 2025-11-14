package com.example.charitybe.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.charitybe.entities.NguoiDung;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessToken.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refreshToken.expiration}")
    private Long refreshTokenExpiration;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String generateAccessToken(NguoiDung user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("userId", user.getId())
                .withClaim("role", user.getVaiTro().getValue())
                .withClaim("email", user.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .sign(getAlgorithm());
    }

    public String generateRefreshToken(NguoiDung user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("userId", user.getId())
                .withClaim("tokenType", "refresh")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .sign(getAlgorithm());
    }

    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public DecodedJWT decodeToken(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        return verifier.verify(token);
    }

    public String extractEmail(String token) {
        return decodeToken(token).getSubject();
    }

    public Long extractUserId(String token) {
        return decodeToken(token).getClaim("userId").asLong();
    }

    public String extractRole(String token) {
        return decodeToken(token).getClaim("role").asString();
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = decodeToken(token).getExpiresAt();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
