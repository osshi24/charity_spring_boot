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
@Table(name = "chi_phi_du_an")
public class ChiPhiDuAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ma_du_an", nullable = false)
    private DuAn maDuAn;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ma_danh_muc_chi_phi", nullable = false)
    private DanhMucChiPhi maDanhMucChiPhi;

    @NotNull
    @Column(name = "mo_ta", nullable = false, length = Integer.MAX_VALUE)
    private String moTa;

    @NotNull
    @Column(name = "so_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal soTien;

    @NotNull
    @Column(name = "ngay_chi", nullable = false)
    private LocalDate ngayChi;

    @Size(max = 500)
    @Column(name = "duong_dan_hoa_don", length = 500)
    private String duongDanHoaDon;

    @Size(max = 255)
    @Column(name = "nha_cung_cap")
    private String nhaCungCap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_phe_duyet")
    private NguoiDung nguoiPheDuyet;

    @Column(name = "ngay_phe_duyet")
    private OffsetDateTime ngayPheDuyet;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nguoi_tao", nullable = false)
    private NguoiDung nguoiTao;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_cap_nhat")
    private OffsetDateTime ngayCapNhat;

}