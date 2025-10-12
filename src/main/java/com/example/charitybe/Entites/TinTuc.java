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
@Table(name = "tin_tuc")
public class TinTuc {
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
    @Column(name = "noi_dung", nullable = false, length = Integer.MAX_VALUE)
    private String noiDung;

    @Size(max = 500)
    @Column(name = "tom_tat", length = 500)
    private String tomTat;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ma_tac_gia", nullable = false)
    private NguoiDung maTacGia;

    @Size(max = 500)
    @Column(name = "anh_dai_dien", length = 500)
    private String anhDaiDien;

    @Column(name = "thu_vien_anh")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> thuVienAnh;

    @Column(name = "the_tag")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> theTag;

    @ColumnDefault("0")
    @Column(name = "luot_xem")
    private Integer luotXem;

    @ColumnDefault("false")
    @Column(name = "la_bai_noi_bat")
    private Boolean laBaiNoiBat;

    @Size(max = 255)
    @Column(name = "tieu_de_seo")
    private String tieuDeSeo;

    @Size(max = 255)
    @Column(name = "mo_ta_seo")
    private String moTaSeo;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;

    @Column(name = "ngay_xuat_ban")
    private OffsetDateTime ngayXuatBan;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_cap_nhat")
    private OffsetDateTime ngayCapNhat;

/*
 TODO [Reverse Engineering] create field to map the 'chuyen_muc' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'tin_tuc'")
    @Column(name = "chuyen_muc", columnDefinition = "chuyen_muc_tin_tuc")
    private Object chuyenMuc;
*/
/*
 TODO [Reverse Engineering] create field to map the 'trang_thai' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'ban_nhap'")
    @Column(name = "trang_thai", columnDefinition = "trang_thai_noi_dung")
    private Object trangThai;
*/
}