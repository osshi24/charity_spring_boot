package com.example.charitybe.entities;

import com.example.charitybe.enums.TrangThaiDuAn;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "du_an")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DuAn {

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

    @NotBlank(message = "Mô tả chi tiết không được để trống")
    @Column(name = "mo_ta_chi_tiet", nullable = false, columnDefinition = "TEXT")
    private String moTaChiTiet;

    @Column(name = "ma_danh_muc")
    private Integer maDanhMuc;

    @DecimalMin(value = "0.0", message = "Số tiền mục tiêu phải >= 0")
    @Column(name = "so_tien_muc_tieu", precision = 15, scale = 2)
    private BigDecimal soTienMucTieu = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Số tiền hiện tại phải >= 0")
    @Column(name = "so_tien_hien_tai", precision = 15, scale = 2)
    private BigDecimal soTienHienTai = BigDecimal.ZERO;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDate ngayBatDau;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @Column(name = "ngay_ket_thuc", nullable = false)
    private LocalDate ngayKetThuc;

    @NotBlank(message = "Địa điểm không được để trống")
    @Column(name = "dia_diem", nullable = false, columnDefinition = "TEXT")
    private String diaDiem;

    @Column(name = "trang_thai", columnDefinition = "trang_thai_du_an")
    @ColumnTransformer(write = "?::trang_thai_du_an")
    private TrangThaiDuAn trangThai = TrangThaiDuAn.BAN_NHAP;

    @Size(max = 500, message = "Thư viện ảnh không được vượt quá 500 ký tự")
    @Column(name = "thu_vien_anh", length = 500)
    private String thuVienAnh;

    @Column(name = "muc_do_yeu_tien")
    private Integer mucDoYeuTien;

    @Column(name = "nguoi_tao")
    private Long nguoiTao;

    @Size(max = 500, message = "Người phê duyệt không được vượt quá 500 ký tự")
    @Column(name = "nguoi_phe_duyet", length = 500)
    private String nguoiPheDuyet;

    @Column(name = "ngay_cap_nhat", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime ngayCapNhat = OffsetDateTime.now();

    @Column(name = "ngay_tao", columnDefinition = "TIMESTAMPTZ", updatable = false)
    private OffsetDateTime ngayTao = OffsetDateTime.now();

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_danh_muc", referencedColumnName = "id", insertable = false, updatable = false)
    private DanhMucDuAn danhMucDuAn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_tao", referencedColumnName = "id", insertable = false, updatable = false)
    private NguoiDung nguoiTaoEntity;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = OffsetDateTime.now();
        }
        if (ngayCapNhat == null) {
            ngayCapNhat = OffsetDateTime.now();
        }
        if (trangThai == null) {
            trangThai = TrangThaiDuAn.BAN_NHAP;
        }
        if (soTienMucTieu == null) {
            soTienMucTieu = BigDecimal.ZERO;
        }
        if (soTienHienTai == null) {
            soTienHienTai = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = OffsetDateTime.now();
    }
}
