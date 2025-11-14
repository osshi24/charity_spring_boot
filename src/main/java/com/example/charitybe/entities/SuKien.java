package com.example.charitybe.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "su_kien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuKien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    @Column(name = "tieu_de", nullable = false, length = 255)
    private String tieuDe;

    @NotBlank(message = "Mô tả không được để trống")
    @Column(name = "mo_ta", nullable = false, columnDefinition = "TEXT")
    private String moTa;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    @Column(name = "thoi_gian_bat_dau", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime thoiGianBatDau;

    @NotBlank(message = "Địa điểm không được để trống")
    @Size(max = 255, message = "Địa điểm không được vượt quá 255 ký tự")
    @Column(name = "dia_diem", nullable = false, length = 255)
    private String diaDiem;

    @Column(name = "nguoi_tao")
    private Long nguoiTao;

    @Column(name = "ngay_tao", columnDefinition = "TIMESTAMPTZ", updatable = false)
    private OffsetDateTime ngayTao = OffsetDateTime.now();

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_tao", referencedColumnName = "id", insertable = false, updatable = false)
    private NguoiDung nguoiTaoEntity;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = OffsetDateTime.now();
        }
    }
}
