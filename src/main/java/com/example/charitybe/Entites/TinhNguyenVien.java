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
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "tinh_nguyen_vien")
public class TinhNguyenVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    private NguoiDung maNguoiDung;

    @Column(name = "ky_nang")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> kyNang;

    @Column(name = "thoi_gian_ranh")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> thoiGianRanh;

    @Column(name = "kinh_nghiem", length = Integer.MAX_VALUE)
    private String kinhNghiem;

    @Column(name = "gioi_thieu", length = Integer.MAX_VALUE)
    private String gioiThieu;

    @ColumnDefault("0.00")
    @Column(name = "diem_danh_gia", precision = 3, scale = 2)
    private BigDecimal diemDanhGia;

    @ColumnDefault("0")
    @Column(name = "tong_gio_dong_gop")
    private Integer tongGioDongGop;

    @Column(name = "ngon_ngu")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> ngonNgu;

    @Size(max = 255)
    @Column(name = "nghe_nghiep")
    private String ngheNghiep;

    @Column(name = "lien_he_khan_cap")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> lienHeKhanCap;

    @ColumnDefault("false")
    @Column(name = "da_kiem_tra_ly_lich")
    private Boolean daKiemTraLyLich;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;

    @Column(name = "ngay_kiem_tra_ly_lich")
    private LocalDate ngayKiemTraLyLich;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_cap_nhat")
    private OffsetDateTime ngayCapNhat;

/*
 TODO [Reverse Engineering] create field to map the 'trang_thai' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'hoat_dong'")
    @Column(name = "trang_thai", columnDefinition = "trang_thai_tnv")
    private Object trangThai;
*/
/*
 TODO [Reverse Engineering] create field to map the 'trinh_do_hoc_van' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "trinh_do_hoc_van", columnDefinition = "trinh_do_hoc_van")
    private Object trinhDoHocVan;
*/
}