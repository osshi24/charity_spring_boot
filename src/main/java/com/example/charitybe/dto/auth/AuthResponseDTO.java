package com.example.charitybe.dto.auth;

import com.example.charitybe.dto.user.UserResponseDTO;

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
    private UserResponseDTO userInfo;

    // User info
    // private Long userId;
    // private String email;
    // private String role;
    // private String ten;
    // private String ho;

    public AuthResponseDTO(String accessToken, String refreshToken, Long expiresIn,
                          UserResponseDTO userInfo) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
        this.expiresIn = expiresIn;
        // this.userId = userId;
        // this.email = email;
        // this.role = role;
        // this.ten = ten;
        // this.ho = ho;
        this.userInfo = userInfo;
    }
}








