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
@Table(name = "tin_nhan_lien_he")
public class TinNhanLienHe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "ten_nguoi_gui", nullable = false)
    private String tenNguoiGui;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 20)
    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Size(max = 255)
    @NotNull
    @Column(name = "chu_de", nullable = false)
    private String chuDe;

    @NotNull
    @Column(name = "noi_dung", nullable = false, length = Integer.MAX_VALUE)
    private String noiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_phu_trach")
    private NguoiDung nguoiPhuTrach;

    @Column(name = "phan_hoi", length = Integer.MAX_VALUE)
    private String phanHoi;

    @Column(name = "thoi_gian_phan_hoi")
    private OffsetDateTime thoiGianPhanHoi;
    @Column(name = "file_dinh_kem")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> fileDinhKem;

    @Column(name = "dia_chi_ip")
    private InetAddress diaChiIp;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;

    @Column(name = "thong_tin_trinh_duyet", length = Integer.MAX_VALUE)
    private String thongTinTrinhDuyet;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_cap_nhat")
    private OffsetDateTime ngayCapNhat;

/*
 TODO [Reverse Engineering] create field to map the 'danh_muc' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'tong_quat'")
    @Column(name = "danh_muc", columnDefinition = "danh_muc_lien_he")
    private Object danhMuc;
*/
/*
 TODO [Reverse Engineering] create field to map the 'muc_do_uu_tien' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'trung_binh'")
    @Column(name = "muc_do_uu_tien", columnDefinition = "muc_do_uu_tien_lh")
    private Object mucDoUuTien;
*/
/*
 TODO [Reverse Engineering] create field to map the 'trang_thai_xu_ly' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'moi'")
    @Column(name = "trang_thai_xu_ly", columnDefinition = "trang_thai_lien_he")
    private Object trangThaiXuLy;
*/
}