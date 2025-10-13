package com.example.charitybe.Entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "tai_khoan_mang_xa_hoi")
public class TaiKhoanMangXaHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    private NguoiDung maNguoiDung;

    @Size(max = 255)
    @NotNull
    @Column(name = "ma_tai_khoan_ngoai", nullable = false)
    private String maTaiKhoanNgoai;

    @Size(max = 255)
    @Column(name = "email_ngoai")
    private String emailNgoai;

    @Size(max = 255)
    @Column(name = "ten_ngoai")
    private String tenNgoai;

    @Column(name = "access_token", length = Integer.MAX_VALUE)
    private String accessToken;

    @Column(name = "refresh_token", length = Integer.MAX_VALUE)
    private String refreshToken;

    @Column(name = "ngay_het_han")
    private OffsetDateTime ngayHetHan;

    @ColumnDefault("true")
    @Column(name = "dang_hoat_dong")
    private Boolean dangHoatDong;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_cap_nhat")
    private OffsetDateTime ngayCapNhat;

/*
 TODO [Reverse Engineering] create field to map the 'nha_cung_cap' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "nha_cung_cap", columnDefinition = "nha_cung_cap_mxh not null")
    private Object nhaCungCap;
*/
}