package com.example.charitybe.Services;

import com.example.charitybe.dto.auth.*;
import com.example.charitybe.entities.NguoiDung;
import com.example.charitybe.entities.RefreshToken;
import com.example.charitybe.enums.TrangThaiNguoiDung;
import com.example.charitybe.enums.VaiTroNguoiDung;
import com.example.charitybe.repositories.NguoiDungRepository;
import com.example.charitybe.repositories.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final NguoiDungRepository nguoiDungRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${jwt.accessToken.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refreshToken.expiration}")
    private Long refreshTokenExpiration;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Check if email already exists
        if (nguoiDungRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Create new user
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setEmail(request.getEmail());
        nguoiDung.setMatKhauHash(passwordEncoder.encode(request.getPassword()));
        nguoiDung.setTen(request.getTen());
        nguoiDung.setHo(request.getHo());
        nguoiDung.setSoDienThoai(request.getSoDienThoai());
        nguoiDung.setDiaChi(request.getDiaChi());
        nguoiDung.setVaiTro(VaiTroNguoiDung.NGUOI_DUNG); // Default role
        nguoiDung.setTrangThai(TrangThaiNguoiDung.HOAT_DONG); // Auto active

        nguoiDung = nguoiDungRepository.save(nguoiDung);

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(nguoiDung);
        String refreshToken = jwtService.generateRefreshToken(nguoiDung);

        // Save refresh token to database
        saveRefreshToken(nguoiDung.getId(), refreshToken);

        return new AuthResponseDTO(
                accessToken,
                refreshToken,
                accessTokenExpiration / 1000, // Convert to seconds
                nguoiDung.getId(),
                nguoiDung.getEmail(),
                nguoiDung.getVaiTro().getValue(),
                nguoiDung.getTen(),
                nguoiDung.getHo()
        );
    }

    @Transactional
    public AuthResponseDTO login(LoginRequestDTO request) {
        // Find user by email
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng"));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), nguoiDung.getMatKhauHash())) {
            throw new RuntimeException("Email hoặc mật khẩu không đúng");
        }

        // Check if user is active
        if (nguoiDung.getTrangThai() != TrangThaiNguoiDung.HOAT_DONG) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(nguoiDung);
        String refreshToken = jwtService.generateRefreshToken(nguoiDung);

        // Delete old refresh tokens for this user
        refreshTokenRepository.deleteByUserId(nguoiDung.getId());

        // Save new refresh token
        saveRefreshToken(nguoiDung.getId(), refreshToken);

        return new AuthResponseDTO(
                accessToken,
                refreshToken,
                accessTokenExpiration / 1000,
                nguoiDung.getId(),
                nguoiDung.getEmail(),
                nguoiDung.getVaiTro().getValue(),
                nguoiDung.getTen(),
                nguoiDung.getHo()
        );
    }

    @Transactional
    public AuthResponseDTO refreshToken(RefreshTokenRequestDTO request) {
        String refreshTokenStr = request.getRefreshToken();

        // Validate token format
        if (!jwtService.validateToken(refreshTokenStr)) {
            throw new RuntimeException("Refresh token không hợp lệ");
        }

        // Find refresh token in database
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new RuntimeException("Refresh token không tồn tại"));

        // Check if token is revoked or expired
        if (refreshToken.getRevoked() || refreshToken.isExpired()) {
            throw new RuntimeException("Refresh token đã hết hạn hoặc bị thu hồi");
        }

        // Get user
        Long userId = jwtService.extractUserId(refreshTokenStr);
        NguoiDung nguoiDung = nguoiDungRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Check if user is active
        if (nguoiDung.getTrangThai() != TrangThaiNguoiDung.HOAT_DONG) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }

        // Generate new access token
        String newAccessToken = jwtService.generateAccessToken(nguoiDung);

        return new AuthResponseDTO(
                newAccessToken,
                refreshTokenStr, // Keep the same refresh token
                accessTokenExpiration / 1000,
                nguoiDung.getId(),
                nguoiDung.getEmail(),
                nguoiDung.getVaiTro().getValue(),
                nguoiDung.getTen(),
                nguoiDung.getHo()
        );
    }

    @Transactional
    public void logout(String refreshTokenStr) {
        refreshTokenRepository.findByToken(refreshTokenStr).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    private void saveRefreshToken(Long userId, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUserId(userId);
        refreshToken.setExpiryDate(OffsetDateTime.now().plusSeconds(refreshTokenExpiration / 1000));
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);
    }
}
