package com.example.charitybe.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "danh_muc_du_an")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DanhMucDuAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = 255, message = "Tên danh mục không được vượt quá 255 ký tự")
    @Column(name = "ten", nullable = false, length = 255)
    private String ten;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "ngay_tao", columnDefinition = "TIMESTAMPTZ", updatable = false)
    private OffsetDateTime ngayTao = OffsetDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = OffsetDateTime.now();
        }
    }
}
