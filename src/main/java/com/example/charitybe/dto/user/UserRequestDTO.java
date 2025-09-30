package com.example.charitybe.dto.user;


import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@ToString(exclude = "password")
public class UserRequestDTO {
    @NotEmpty(message = "Username không được để trống")
    private String userName;

    @NotEmpty(message = "Fullname không được để trống")
    private String fullName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate birthDate;

    @NotEmpty(message = "Giới tính không được để trống")
    private String gender;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotEmpty(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    private String password;

    private Long role;
}
