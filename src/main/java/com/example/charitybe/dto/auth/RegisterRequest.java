package com.example.charitybe.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "Email không được để trống")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
         message = "Email không đúng định dạng.")
    @Size(max = 255)
    private String email;
    
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 255, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
    
    @NotBlank(message = "Họ không được để trống")
    @Size(max = 100)
    private String ho;
    
    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100)
    private String ten;
    
    @Pattern(regexp = "^0[0-9]{9,10}$", 
         message = "Số điện thoại phải là 10 hoặc 11 chữ số, bắt đầu bằng số 0 và chỉ chứa ký tự số.")
    private String so_dien_thoai;
}