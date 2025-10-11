package com.example.charitybe.Services.auth;

import java.time.Instant;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.charitybe.Entites.User;
import com.example.charitybe.Repository.UserRepository;
import com.example.charitybe.dto.auth.AuthResponseDTO;
import com.example.charitybe.dto.auth.LoginRequestDTO;
import com.example.charitybe.dto.auth.RegisterRequest;
import com.example.charitybe.dto.user.UserResponseDTO;
import com.example.charitybe.enums.TrangThaiNguoiDung;
import com.example.charitybe.enums.VaiTroEnum;
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
        System.out.println("ưetwerwrwrwerew" + loginRequestDTO);
        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // System.out.println("usrrr" + user);
        // if (!passwordEncoder.matches(password, user.getPasswordHash())) {
        // throw new BadCredentialsException("Mật khẩu không hợp lệ");
        // }
        UserResponseDTO response = userMapper.toDTO(user);
        System.out.println("resposnreeee" + response);
        String accessToken = jwtService.generateAccessToken(user.getId(), "Admin");
        System.out.println("accrssToken" + accessToken);
        return new AuthResponseDTO<>(accessToken, response);
    }

    public UserResponseDTO register(RegisterRequest request) {
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setMat_khau_hash(passwordEncoder.encode(request.getPassword()));
        newUser.setHo(request.getHo());
        newUser.setTen(request.getTen());
        newUser.setSo_dien_thoai(request.getSo_dien_thoai());
        newUser.setVai_tro(VaiTroEnum.nguoi_dung);
        newUser.setTrang_thai(TrangThaiNguoiDung.hoat_dong);
        Instant now = Instant.now();
        newUser.setCreangay_taotedAt(now); // Đặt ngay_tao
        newUser.setNgay_cap_nhat(now);  // 
        userRepository.save(newUser);

        return userMapper.toDTO(newUser);
    }

}
