package com.example.charitybe.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "lien_he")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LienHe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Tên người gửi không được để trống")
    @Size(max = 255, message = "Tên người gửi không được vượt quá 255 ký tự")
    @Column(name = "ten_nguoi_gui", nullable = false, length = 255)
    private String tenNguoiGui;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 255, message = "Email không được vượt quá 255 ký tự")
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    @Column(name = "tieu_de", nullable = false, length = 255)
    private String tieuDe;

    @NotBlank(message = "Nội dung không được để trống")
    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "ngay_tao", columnDefinition = "TIMESTAMPTZ", updatable = false)
    private OffsetDateTime ngayTao = OffsetDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = OffsetDateTime.now();
        }
    }
}
