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
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "bao_cao_tai_chinh")
public class BaoCaoTaiChinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "ngay_bat_dau_ky", nullable = false)
    private LocalDate ngayBatDauKy;

    @NotNull
    @Column(name = "ngay_ket_thuc_ky", nullable = false)
    private LocalDate ngayKetThucKy;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ma_du_an")
    private DuAn maDuAn;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "tong_quyen_gop", nullable = false, precision = 15, scale = 2)
    private BigDecimal tongQuyenGop;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "tong_chi_phi", nullable = false, precision = 15, scale = 2)
    private BigDecimal tongChiPhi;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "chi_phi_quan_ly", nullable = false, precision = 15, scale = 2)
    private BigDecimal chiPhiQuanLy;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "chi_phi_du_an", nullable = false, precision = 15, scale = 2)
    private BigDecimal chiPhiDuAn;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "chi_phi_van_hanh", nullable = false, precision = 15, scale = 2)
    private BigDecimal chiPhiVanHanh;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "so_du_quy", nullable = false, precision = 15, scale = 2)
    private BigDecimal soDuQuy;

    @Size(max = 500)
    @Column(name = "duong_dan_file_bao_cao", length = 500)
    private String duongDanFileBaoCao;

    @Column(name = "tom_tat", length = Integer.MAX_VALUE)
    private String tomTat;

    @Column(name = "ghi_chu", length = Integer.MAX_VALUE)
    private String ghiChu;

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

    @Column(name = "ngay_xuat_ban")
    private OffsetDateTime ngayXuatBan;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_cap_nhat")
    private OffsetDateTime ngayCapNhat;

/*
 TODO [Reverse Engineering] create field to map the 'loai_bao_cao' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "loai_bao_cao", columnDefinition = "loai_bao_cao not null")
    private Object loaiBaoCao;
*/
/*
 TODO [Reverse Engineering] create field to map the 'trang_thai' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'ban_nhap'")
    @Column(name = "trang_thai", columnDefinition = "trang_thai_noi_dung")
    private Object trangThai;
*/
}