package com.example.charitybe.Services.auth;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.charitybe.Entites.NguoiDung;
import com.example.charitybe.Repository.UserRepository;
import com.example.charitybe.dto.auth.AuthResponseDTO;
import com.example.charitybe.dto.auth.LoginRequestDTO;
import com.example.charitybe.dto.auth.RegisterRequest;
import com.example.charitybe.dto.user.UserResponseDTO;
import com.example.charitybe.enums.TrangThaiNguoiDungEnum;
import com.example.charitybe.enums.VaiTroEnum;
import com.example.charitybe.exceptions.DuplicateEmailException;
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
        NguoiDung user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // System.out.println("usrrr" + user);
        if (!passwordEncoder.matches(password, user.getMatKhauHash())) {
            throw new BadCredentialsException("Mật khẩu không hợp lệ");
        }
        UserResponseDTO response = userMapper.toDTO(user);
        System.out.println("resposnreeee" + response);
        String accessToken = jwtService.generateAccessToken(user.getId());
        // String accessToken = jwtService.generateAccessToken(user.getId());

        System.out.println("accrssToken" + accessToken);
        return new AuthResponseDTO<>(accessToken, response);
    }

    public UserResponseDTO register(RegisterRequest request) {
        Optional<NguoiDung> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            // Chỉ ném exception nghiệp vụ
            throw new DuplicateEmailException("Email address already in use.");
        }
        NguoiDung newUser = new NguoiDung();
        newUser.setEmail(request.getEmail());
        newUser.setMatKhauHash(passwordEncoder.encode(request.getPassword()));
        newUser.setHo(request.getHo());
        newUser.setTen(request.getTen());
        newUser.setSoDienThoai(request.getSo_dien_thoai());
        newUser.setVaiTro(VaiTroEnum.nguoi_dung);

        newUser.setTrangThai(TrangThaiNguoiDungEnum.hoat_dong);
        Instant now = Instant.now();
        OffsetDateTime offsetNow = now.atOffset(ZoneOffset.UTC);

        newUser.setNgayTao(offsetNow);
        newUser.setNgayCapNhat(offsetNow); // <-- Dòng này đã được sửa
        userRepository.save(newUser);

        return userMapper.toDTO(newUser);
    }

}
