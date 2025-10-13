package com.example.charitybe.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.charitybe.Services.auth.AuthService;
import com.example.charitybe.dto.ApiResponseDTO;
import com.example.charitybe.dto.auth.AuthResponseDTO;
import com.example.charitybe.dto.auth.LoginRequestDTO;
import com.example.charitybe.dto.auth.RegisterRequest;
import com.example.charitybe.dto.user.UserResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO<UserResponseDTO>>> login(
            @Valid @RequestBody LoginRequestDTO request) {
        System.out.println("requersttttttttttttttttttttttttttttttttttttttt" + request);
        AuthResponseDTO<UserResponseDTO> authResponseDTO = authService.login(request);
        ApiResponseDTO<AuthResponseDTO<UserResponseDTO>> response = new ApiResponseDTO<>(200, authResponseDTO,
                "Login successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponseDTO userResponseDTO = authService.register(request);
        ApiResponseDTO<UserResponseDTO> response = new ApiResponseDTO<>(200, userResponseDTO, "Register successful");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }

}
