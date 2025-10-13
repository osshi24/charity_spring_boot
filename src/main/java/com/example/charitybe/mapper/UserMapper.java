package com.example.charitybe.mapper;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.charitybe.Entites.NguoiDung;
import com.example.charitybe.dto.user.UserResponseDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserMapper {

    public UserResponseDTO toDTO(NguoiDung user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setHo(user.getHo());
        dto.setTen(user.getTen());
        dto.setSo_dien_thoai(user.getSoDienThoai());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (user.getNgaySinh() != null) {
            dto.setNgay_sinh(user.getNgaySinh().format(formatter));
        } else {
            dto.setNgay_sinh(null); // hoặc chuỗi rỗng ""
        }
        dto.setVai_tro(user.getVaiTro().toString());
        return dto;
    }

    public NguoiDung toEntity(UserResponseDTO dto) {
        NguoiDung user = new NguoiDung();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setTen(dto.getTen());
        user.setSoDienThoai(dto.getSo_dien_thoai());
        user.setHo(dto.getHo());
        // user.setVai_tro(dto.getVai_tro());
        return user;
    }

}