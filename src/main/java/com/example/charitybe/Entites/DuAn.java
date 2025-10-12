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
@Table(name = "du_an")
public class DuAn {
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ma_danh_muc", nullable = false)
    private DanhMucDuAn maDanhMuc;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "so_tien_muc_tieu", nullable = false, precision = 15, scale = 2)
    private BigDecimal soTienMucTieu;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "so_tien_hien_tai", nullable = false, precision = 15, scale = 2)
    private BigDecimal soTienHienTai;

    @NotNull
    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDate ngayBatDau;

    @NotNull
    @Column(name = "ngay_ket_thuc", nullable = false)
    private LocalDate ngayKetThuc;

    @Size(max = 255)
    @Column(name = "dia_diem")
    private String diaDiem;

    @ColumnDefault("0")
    @Column(name = "so_nguoi_thu_huong")
    private Integer soNguoiThuHuong;

    @Size(max = 500)
    @Column(name = "anh_dai_dien", length = 500)
    private String anhDaiDien;

    @Column(name = "thu_vien_anh")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> thuVienAnh;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nguoi_tao", nullable = false)
    private NguoiDung nguoiTao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_phe_duyet")
    private NguoiDung nguoiPheDuyet;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;

    @Column(name = "thoi_gian_phe_duyet")
    private OffsetDateTime thoiGianPheDuyet;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_cap_nhat")
    private OffsetDateTime ngayCapNhat;

/*
 TODO [Reverse Engineering] create field to map the 'trang_thai' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'ban_nhap'")
    @Column(name = "trang_thai", columnDefinition = "trang_thai_du_an")
    private Object trangThai;
*/
/*
 TODO [Reverse Engineering] create field to map the 'muc_do_uu_tien' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'trung_binh'")
    @Column(name = "muc_do_uu_tien", columnDefinition = "muc_do_uu_tien")
    private Object mucDoUuTien;
*/
}