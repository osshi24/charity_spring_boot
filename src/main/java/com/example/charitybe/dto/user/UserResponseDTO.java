package com.example.charitybe.dto.user;


import lombok.*;

@Data
public class UserResponseDTO {
    private Long id;
    private String ho;
    private String ten;
    private String ngay_sinh;
    private String email;
    private String vai_tro;
    private String so_dien_thoai;
    // private RoleResponseDTO role;
}