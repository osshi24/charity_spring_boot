package com.example.charitybe.Entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "dang_ky_nhan_tin")
public class DangKyNhanTin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @Column(name = "ten")
    private String ten;

    @Column(name = "tuy_chon_nhan_tin")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> tuyChonNhanTin;

    @Size(max = 100)
    @Column(name = "nguon_dang_ky", length = 100)
    private String nguonDangKy;

    @Size(max = 255)
    @Column(name = "ma_xac_thuc")
    private String maXacThuc;

    @Column(name = "ngay_xac_thuc")
    private OffsetDateTime ngayXacThuc;

    @Column(name = "ngay_huy_dang_ky")
    private OffsetDateTime ngayHuyDangKy;

    @Column(name = "lan_gui_email_cuoi")
    private OffsetDateTime lanGuiEmailCuoi;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_cap_nhat")
    private OffsetDateTime ngayCapNhat;

/*
 TODO [Reverse Engineering] create field to map the 'trang_thai' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'hoat_dong'")
    @Column(name = "trang_thai", columnDefinition = "trang_thai_dang_ky")
    private Object trangThai;
*/
}