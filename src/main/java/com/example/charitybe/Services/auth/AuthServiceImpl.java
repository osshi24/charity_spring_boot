package com.example.charitybe.Services.auth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.charitybe.Entites.User;
import com.example.charitybe.Repository.UserRepository;
import com.example.charitybe.dto.auth.AuthResponseDTO;
import com.example.charitybe.dto.auth.LoginRequestDTO;
import com.example.charitybe.dto.user.UserResponseDTO;
import com.example.charitybe.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthResponseDTO<UserResponseDTO> login(LoginRequestDTO loginRequestDTO) {
        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // System.out.println("usrrr" + user);
        // if (!passwordEncoder.matches(password, user.getPasswordHash())) {
        //     throw new BadCredentialsException("Mật khẩu không hợp lệ");
        // }
        UserResponseDTO response = userMapper.toDTO(user);
        System.out.println("resposnreeee" + response);
        String accessToken = jwtService.generateAccessToken(user.getId(), "Admin");
        System.out.println("accrssToken" + accessToken);
        return new AuthResponseDTO<>(accessToken, response);
    }

}
