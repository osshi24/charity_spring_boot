package com.example.charitybe.Entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.net.InetAddress;
import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "nhat_ky_kiem_toan")
public class NhatKyKiemToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung maNguoiDung;

    @Size(max = 100)
    @NotNull
    @Column(name = "hanh_dong", nullable = false, length = 100)
    private String hanhDong;

    @Size(max = 100)
    @NotNull
    @Column(name = "ten_bang", nullable = false, length = 100)
    private String tenBang;

    @Column(name = "ma_ban_ghi")
    private Long maBanGhi;

    @Column(name = "gia_tri_cu")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> giaTriCu;

    @Column(name = "gia_tri_moi")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> giaTriMoi;

    @Column(name = "mo_ta", length = Integer.MAX_VALUE)
    private String moTa;

    @Column(name = "dia_chi_ip")
    private InetAddress diaChiIp;

    @Column(name = "thong_tin_trinh_duyet", length = Integer.MAX_VALUE)
    private String thongTinTrinhDuyet;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;

}