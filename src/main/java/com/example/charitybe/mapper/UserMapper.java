package com.example.charitybe.mapper;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.charitybe.Entites.User;
import com.example.charitybe.dto.user.UserResponseDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserMapper {

    public UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setHo(user.getHo());
        dto.setTen(user.getTen());
        dto.setSo_dien_thoai(user.getSo_dien_thoai());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (user.getNgay_sinh() != null) {
            dto.setNgay_sinh(user.getNgay_sinh().format(formatter));
        } else {
            dto.setNgay_sinh(null); // hoặc chuỗi rỗng ""
        }
        dto.setVai_tro(user.getVai_tro().name());
        return dto;
    }

    public User toEntity(UserResponseDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setTen(dto.getTen());
        user.setSo_dien_thoai(dto.getSo_dien_thoai());
        user.setHo(dto.getHo());
        // user.setVai_tro(dto.getVai_tro());
        return user;
    }

}