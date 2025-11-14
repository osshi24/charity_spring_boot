package com.example.charitybe.entities;

import com.example.charitybe.enums.TrangThaiNguoiDung;
import com.example.charitybe.enums.VaiTroNguoiDung;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import java.time.OffsetDateTime;

@Entity
@Table(name = "nguoi_dung")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Column(name = "mat_khau_hash", nullable = false, length = 255)
    private String matKhauHash;

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
    @Column(name = "ten", nullable = false, length = 100)
    private String ten;

    @NotBlank(message = "Họ không được để trống")
    @Size(max = 100, message = "Họ không được vượt quá 100 ký tự")
    @Column(name = "ho", nullable = false, length = 100)
    private String ho;

    @Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "dia_chi", columnDefinition = "TEXT")
    private String diaChi;

    @Column(name = "vai_tro", columnDefinition = "vai_tro_nguoi_dung")
    @ColumnTransformer(write = "?::vai_tro_nguoi_dung")
    private VaiTroNguoiDung vaiTro = VaiTroNguoiDung.NGUOI_DUNG;

    @Column(name = "trang_thai", columnDefinition = "trang_thai_nguoi_dung")
    @ColumnTransformer(write = "?::trang_thai_nguoi_dung")
    private TrangThaiNguoiDung trangThai = TrangThaiNguoiDung.HOAT_DONG;

    @Column(name = "ngay_tao", columnDefinition = "TIMESTAMPTZ", updatable = false)
    private OffsetDateTime ngayTao = OffsetDateTime.now();

    // Relationships will be added after all entities are created

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = OffsetDateTime.now();
        }
        if (vaiTro == null) {
            vaiTro = VaiTroNguoiDung.NGUOI_DUNG;
        }
        if (trangThai == null) {
            trangThai = TrangThaiNguoiDung.HOAT_DONG;
        }
    }
}
