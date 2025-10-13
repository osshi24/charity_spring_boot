package com.example.charitybe.Entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "cap_nhat_du_an")
public class CapNhatDuAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ma_du_an", nullable = false)
    private DuAn maDuAn;

    @Size(max = 255)
    @NotNull
    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @NotNull
    @Column(name = "noi_dung", nullable = false, length = Integer.MAX_VALUE)
    private String noiDung;

    @Column(name = "danh_sach_anh")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> danhSachAnh;

    @ColumnDefault("0.00")
    @Column(name = "phan_tram_hoan_thanh", precision = 5, scale = 2)
    private BigDecimal phanTramHoanThanh;

    @ColumnDefault("0.00")
    @Column(name = "so_tien_da_su_dung", precision = 15, scale = 2)
    private BigDecimal soTienDaSuDung;

    @ColumnDefault("0")
    @Column(name = "so_nguoi_da_tiep_can")
    private Integer soNguoiDaTiepCan;

    @ColumnDefault("true")
    @Column(name = "la_cong_khai")
    private Boolean laCongKhai;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nguoi_tao", nullable = false)
    private NguoiDung nguoiTao;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;

/*
 TODO [Reverse Engineering] create field to map the 'loai_cap_nhat' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'tien_do'")
    @Column(name = "loai_cap_nhat", columnDefinition = "loai_cap_nhat")
    private Object loaiCapNhat;
*/
}