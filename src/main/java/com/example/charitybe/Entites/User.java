package com.example.charitybe.Entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.example.charitybe.enums.TrangThaiNguoiDung;
import com.example.charitybe.enums.VaiTroEnum;

import java.time.Instant;
import java.time.LocalDate;
 
@Getter
@Setter
@Entity
@Table(name = "nguoi_dung")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "mat_khau_hash", nullable = false)
    private String mat_khau_hash;

    @Column(name = "ten", nullable = false, length = 100)
    private String ten;

    @Column(name = "ho", nullable = false, length = 100)
    private String ho;

    @Column(name = "so_dien_thoai", length = 20)
    private String so_dien_thoai;

    @Column(name = "dia_chi", length = Integer.MAX_VALUE)
    private String dia_chi;

    @Column(name = "ngay_sinh")
    private LocalDate ngay_sinh;

    @ColumnDefault("'user'")
    @Column(name = "vai_tro", length = 20)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private VaiTroEnum vai_tro;

    @ColumnDefault("'active'")
    @Column(name = "trang_thai", length = 20)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private TrangThaiNguoiDung trang_thai;

    @ColumnDefault("false")
    @Column(name = "email_da_xac_thuc")
    private Boolean email_da_xac_thuc;

    @Column(name = "thoi_gian_xac_thuc_email")
    private Instant thoi_gian_xac_thuc_email;

    @Column(name = "token_ghi_nho", length = 100)
    private String token_ghi_nho;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private Instant creangay_taotedAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_cap_nhat")
    private Instant ngay_cap_nhat;

}