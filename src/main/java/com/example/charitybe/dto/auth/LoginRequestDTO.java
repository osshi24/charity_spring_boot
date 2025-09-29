package com.example.charitybe.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@ToString(exclude = "password")
public class LoginRequestDTO {
    @NotEmpty(message = "Email không được để trống")
    private String email;

    @NotEmpty(message = "Password không được để trống")
    private String password;
}
