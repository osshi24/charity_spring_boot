package com.example.charitybe.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;

    // User info
    private Long userId;
    private String email;
    private String role;
    private String ten;
    private String ho;

    public AuthResponseDTO(String accessToken, String refreshToken, Long expiresIn,
                          Long userId, String email, String role, String ten, String ho) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
        this.expiresIn = expiresIn;
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.ten = ten;
        this.ho = ho;
    }
}








