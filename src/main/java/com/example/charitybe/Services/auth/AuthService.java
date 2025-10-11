package com.example.charitybe.Services.auth;

import com.example.charitybe.dto.auth.AuthResponseDTO;
import com.example.charitybe.dto.auth.LoginRequestDTO;
import com.example.charitybe.dto.auth.RegisterRequest;
import com.example.charitybe.dto.user.UserResponseDTO;

public interface AuthService {
    public AuthResponseDTO<UserResponseDTO> login(LoginRequestDTO loginRequestDTO);
    public UserResponseDTO register(RegisterRequest request);
}
