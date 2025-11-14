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
@Table(name = "tai_khoan_thanh_toan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Mã dự án không được để trống")
    @Column(name = "ma_du_an", nullable = false)
    private Long maDuAn;

    @NotBlank(message = "Tên tài khoản không được để trống")
    @Size(max = 255, message = "Tên tài khoản không được vượt quá 255 ký tự")
    @Column(name = "ten_tai_khoan", nullable = false, length = 255)
    private String tenTaiKhoan;

    @NotBlank(message = "Số tài khoản không được để trống")
    @Size(max = 50, message = "Số tài khoản không được vượt quá 50 ký tự")
    @Column(name = "so_tai_khoan", nullable = false, length = 50)
    private String soTaiKhoan;

    @NotBlank(message = "Tên ngân hàng không được để trống")
    @Size(max = 255, message = "Tên ngân hàng không được vượt quá 255 ký tự")
    @Column(name = "ten_ngan_hang", nullable = false, length = 255)
    private String tenNganHang;

    @Size(max = 255, message = "Chi nhánh ngân hàng không được vượt quá 255 ký tự")
    @Column(name = "chi_nhanh_ngan_hang", length = 255)
    private String chiNhanhNganHang;

    @Column(name = "la_tai_khoan_mac_dinh")
    private Boolean laTaiKhoanMacDinh = true;

    @Size(max = 20, message = "Trạng thái không được vượt quá 20 ký tự")
    @Column(name = "trang_thai", length = 20)
    private String trangThai = "hoat_dong";

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "ngay_tao", columnDefinition = "TIMESTAMPTZ", updatable = false)
    private OffsetDateTime ngayTao = OffsetDateTime.now();

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_du_an", referencedColumnName = "id", insertable = false, updatable = false)
    private DuAn duAn;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = OffsetDateTime.now();
        }
        if (laTaiKhoanMacDinh == null) {
            laTaiKhoanMacDinh = true;
        }
        if (trangThai == null) {
            trangThai = "hoat_dong";
        }
    }
}
