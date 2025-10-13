package com.example.charitybe.Entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "quyen_gop")
public class QuyenGop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung maNguoiDung;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ma_du_an", nullable = false)
    private DuAn maDuAn;

    @NotNull
    @Column(name = "so_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal soTien;

    @Size(max = 3)
    @ColumnDefault("'VND'")
    @Column(name = "don_vi_tien_te", length = 3)
    private String donViTienTe;

    @Size(max = 255)
    @Column(name = "ma_giao_dich")
    private String maGiaoDich;

    @Size(max = 255)
    @Column(name = "ma_giao_dich_ngoai")
    private String maGiaoDichNgoai;

    @ColumnDefault("false")
    @Column(name = "la_quyen_gop_dinh_ky")
    private Boolean laQuyenGopDinhKy;

    @Column(name = "ngay_thanh_toan_tiep_theo")
    private LocalDate ngayThanhToanTiepTheo;

    @Size(max = 500)
    @Column(name = "duong_dan_bien_lai", length = 500)
    private String duongDanBienLai;

    @Size(max = 255)
    @Column(name = "ten_nguoi_quyen_gop")
    private String tenNguoiQuyenGop;

    @Size(max = 255)
    @Column(name = "email_nguoi_quyen_gop")
    private String emailNguoiQuyenGop;

    @Size(max = 20)
    @Column(name = "sdt_nguoi_quyen_gop", length = 20)
    private String sdtNguoiQuyenGop;

    @ColumnDefault("false")
    @Column(name = "la_quyen_gop_an_danh")
    private Boolean laQuyenGopAnDanh;

    @Column(name = "loi_nhan", length = Integer.MAX_VALUE)
    private String loiNhan;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;

    @ColumnDefault("0.00")
    @Column(name = "phi_giao_dich", precision = 10, scale = 2)
    private BigDecimal phiGiaoDich;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_cap_nhat")
    private OffsetDateTime ngayCapNhat;

    @ColumnDefault("(so_tien - phi_giao_dich)")
    @Column(name = "so_tien_thuc_nhan", precision = 15, scale = 2)
    private BigDecimal soTienThucNhan;
    @Column(name = "ngay_hoan_thanh")
    private OffsetDateTime ngayHoanThanh;

/*
 TODO [Reverse Engineering] create field to map the 'phuong_thuc_thanh_toan' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "phuong_thuc_thanh_toan", columnDefinition = "phuong_thuc_thanh_toan not null")
    private Object phuongThucThanhToan;
*/
/*
 TODO [Reverse Engineering] create field to map the 'trang_thai_thanh_toan' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'cho_xu_ly'")
    @Column(name = "trang_thai_thanh_toan", columnDefinition = "trang_thai_thanh_toan")
    private Object trangThaiThanhToan;
*/
/*
 TODO [Reverse Engineering] create field to map the 'tan_suat_quyen_gop' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "tan_suat_quyen_gop", columnDefinition = "tan_suat_quyen_gop")
    private Object tanSuatQuyenGop;
*/
}