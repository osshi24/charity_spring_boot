package com.example.charitybe.Entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "su_kien")
public class SuKien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @Size(max = 255)
    @NotNull
    @Column(name = "duong_dan", nullable = false)
    private String duongDan;

    @NotNull
    @Column(name = "mo_ta", nullable = false, length = Integer.MAX_VALUE)
    private String moTa;

    @Size(max = 500)
    @Column(name = "mo_ta_ngan", length = 500)
    private String moTaNgan;

    @NotNull
    @Column(name = "thoi_gian_bat_dau", nullable = false)
    private OffsetDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc")
    private OffsetDateTime thoiGianKetThuc;

    @Size(max = 255)
    @NotNull
    @Column(name = "dia_diem", nullable = false)
    private String diaDiem;

    @Column(name = "dia_chi_chi_tiet", length = Integer.MAX_VALUE)
    private String diaChiChiTiet;

    @Column(name = "vi_do", precision = 10, scale = 8)
    private BigDecimal viDo;

    @Column(name = "kinh_do", precision = 11, scale = 8)
    private BigDecimal kinhDo;

    @Column(name = "so_nguoi_toi_da")
    private Integer soNguoiToiDa;

    @ColumnDefault("0")
    @Column(name = "so_nguoi_hien_tai")
    private Integer soNguoiHienTai;

    @Column(name = "han_dang_ky")
    private OffsetDateTime hanDangKy;

    @ColumnDefault("0.00")
    @Column(name = "phi_tham_gia", precision = 10, scale = 2)
    private BigDecimal phiThamGia;

    @Size(max = 500)
    @Column(name = "anh_dai_dien", length = 500)
    private String anhDaiDien;

    @Column(name = "thu_vien_anh")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> thuVienAnh;

    @Size(max = 255)
    @Column(name = "nguoi_lien_he")
    private String nguoiLienHe;

    @Size(max = 20)
    @Column(name = "sdt_lien_he", length = 20)
    private String sdtLienHe;

    @Size(max = 255)
    @Column(name = "email_lien_he")
    private String emailLienHe;

    @Column(name = "yeu_cau_tham_gia", length = Integer.MAX_VALUE)
    private String yeuCauThamGia;

    @Column(name = "quyen_loi", length = Integer.MAX_VALUE)
    private String quyenLoi;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nguoi_tao", nullable = false)
    private NguoiDung nguoiTao;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_cap_nhat")
    private OffsetDateTime ngayCapNhat;

/*
 TODO [Reverse Engineering] create field to map the 'trang_thai' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'sap_dien_ra'")
    @Column(name = "trang_thai", columnDefinition = "trang_thai_su_kien")
    private Object trangThai;
*/
/*
 TODO [Reverse Engineering] create field to map the 'loai_su_kien' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'khac'")
    @Column(name = "loai_su_kien", columnDefinition = "loai_su_kien")
    private Object loaiSuKien;
*/
}