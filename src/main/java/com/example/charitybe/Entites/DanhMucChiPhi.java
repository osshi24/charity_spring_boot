package com.example.charitybe.Entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "danh_muc_chi_phi")
public class DanhMucChiPhi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "ten", nullable = false)
    private String ten;

    @Column(name = "mo_ta", length = Integer.MAX_VALUE)
    private String moTa;

    @ColumnDefault("false")
    @Column(name = "la_chi_phi_quan_ly")
    private Boolean laChiPhiQuanLy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_danh_muc_cha")
    private DanhMucChiPhi maDanhMucCha;

    @ColumnDefault("0")
    @Column(name = "thu_tu_sap_xep")
    private Integer thuTuSapXep;

    @ColumnDefault("true")
    @Column(name = "dang_hoat_dong")
    private Boolean dangHoatDong;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;

}