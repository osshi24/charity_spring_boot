package com.example.charitybe.Entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

// import com.example.charitybe.enums.TrangThaiNguoiDungConverter;
import com.example.charitybe.enums.TrangThaiNguoiDungEnum;
// import com.example.charitybe.enums.VaiTroConverter;
import com.example.charitybe.enums.VaiTroEnum;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "nguoi_dung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "mat_khau_hash", nullable = false)
    private String matKhauHash;

    @Size(max = 100)
    @NotNull
    @Column(name = "ten", nullable = false, length = 100)
    private String ten;

    @Size(max = 100)
    @NotNull
    @Column(name = "ho", nullable = false, length = 100)
    private String ho;

    @Size(max = 20)
    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "dia_chi", length = Integer.MAX_VALUE)
    private String diaChi;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @ColumnDefault("false")
    @Column(name = "email_da_xac_thuc")
    private Boolean emailDaXacThuc;

    @Column(name = "thoi_gian_xac_thuc_email")
    private OffsetDateTime thoiGianXacThucEmail;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;

    @Size(max = 100)
    @Column(name = "token_ghi_nho", length = 100)
    private String tokenGhiNho;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_cap_nhat")
    private OffsetDateTime ngayCapNhat;

    // @Convert(converter = VaiTroConverter.class)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'nguoi_dung'")
    @Column(name = "vai_tro")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private VaiTroEnum vaiTro;

    // @Convert(converter = TrangThaiNguoiDungConverter.class)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'hoat_dong'")
    @Column(name = "trang_thai")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private TrangThaiNguoiDungEnum trangThai;

}