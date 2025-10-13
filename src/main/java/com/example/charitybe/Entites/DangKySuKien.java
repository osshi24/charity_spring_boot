package com.example.charitybe.Entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "dang_ky_su_kien")
public class DangKySuKien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ma_su_kien", nullable = false)
    private SuKien maSuKien;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung maNguoiDung;

    @Size(max = 255)
    @Column(name = "ten_khach")
    private String tenKhach;

    @Size(max = 255)
    @Column(name = "email_khach")
    private String emailKhach;

    @Size(max = 20)
    @Column(name = "sdt_khach", length = 20)
    private String sdtKhach;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_dang_ky")
    private OffsetDateTime ngayDangKy;

    @ColumnDefault("0.00")
    @Column(name = "so_tien_thanh_toan", precision = 10, scale = 2)
    private BigDecimal soTienThanhToan;

    @Column(name = "yeu_cau_dac_biet", length = Integer.MAX_VALUE)
    private String yeuCauDacBiet;

    @Column(name = "ghi_chu", length = Integer.MAX_VALUE)
    private String ghiChu;
    @Column(name = "phan_hoi_su_kien", length = Integer.MAX_VALUE)
    private String phanHoiSuKien;

    @Column(name = "thoi_gian_check_in")
    private OffsetDateTime thoiGianCheckIn;
    @ColumnDefault("0.00")
    @Column(name = "diem_danh_gia_su_kien", precision = 3, scale = 2)
    private BigDecimal diemDanhGiaSuKien;

/*
 TODO [Reverse Engineering] create field to map the 'trang_thai' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'da_dang_ky'")
    @Column(name = "trang_thai", columnDefinition = "trang_thai_dang_ky_sk")
    private Object trangThai;
*/
/*
 TODO [Reverse Engineering] create field to map the 'trang_thai_thanh_toan' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'mien_phi'")
    @Column(name = "trang_thai_thanh_toan", columnDefinition = "trang_thai_thanh_toan_sk")
    private Object trangThaiThanhToan;
*/
}