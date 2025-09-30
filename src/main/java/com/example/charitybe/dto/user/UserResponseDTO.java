package com.example.charitybe.dto.user;


import lombok.*;

@Data
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String birthDate;
    private String gender;
    private String email;
    // private RoleResponseDTO role;
}