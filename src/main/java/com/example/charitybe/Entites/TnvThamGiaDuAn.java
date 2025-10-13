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
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "tnv_tham_gia_du_an")
public class TnvThamGiaDuAn {
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ma_tinh_nguyen_vien", nullable = false)
    private TinhNguyenVien maTinhNguyenVien;

    @Size(max = 255)
    @NotNull
    @Column(name = "vai_tro", nullable = false)
    private String vaiTro;

    @Column(name = "trach_nhiem", length = Integer.MAX_VALUE)
    private String trachNhiem;

    @ColumnDefault("0")
    @Column(name = "gio_dong_gop")
    private Integer gioDongGop;

    @ColumnDefault("0.00")
    @Column(name = "diem_danh_gia_hieu_suat", precision = 3, scale = 2)
    private BigDecimal diemDanhGiaHieuSuat;

    @Column(name = "phan_hoi", length = Integer.MAX_VALUE)
    private String phanHoi;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tham_gia")
    private OffsetDateTime ngayThamGia;
    @Column(name = "ngay_hoan_thanh")
    private OffsetDateTime ngayHoanThanh;

/*
 TODO [Reverse Engineering] create field to map the 'trang_thai' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'da_ung_tuyen'")
    @Column(name = "trang_thai", columnDefinition = "trang_thai_tnv_du_an")
    private Object trangThai;
*/
}