package com.example.charitybe.mapper;

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
       
        return dto;
    }

  


}