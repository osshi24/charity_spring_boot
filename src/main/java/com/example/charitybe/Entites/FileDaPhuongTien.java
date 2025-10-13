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
@Table(name = "file_da_phuong_tien")
public class FileDaPhuongTien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "ten_file", nullable = false)
    private String tenFile;

    @Size(max = 255)
    @NotNull
    @Column(name = "ten_goc", nullable = false)
    private String tenGoc;

    @Size(max = 500)
    @NotNull
    @Column(name = "duong_dan", nullable = false, length = 500)
    private String duongDan;

    @NotNull
    @Column(name = "kich_thuoc", nullable = false)
    private Long kichThuoc;

    @Size(max = 100)
    @NotNull
    @Column(name = "loai_mime", nullable = false, length = 100)
    private String loaiMime;

    @Column(name = "chieu_rong")
    private Integer chieuRong;

    @Column(name = "chieu_cao")
    private Integer chieuCao;

    @Column(name = "thoi_luong")
    private Integer thoiLuong;

    @Size(max = 255)
    @Column(name = "chu_thich_anh")
    private String chuThichAnh;

    @Column(name = "chu_thich", length = Integer.MAX_VALUE)
    private String chuThich;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nguoi_tai_len", nullable = false)
    private NguoiDung nguoiTaiLen;

    @ColumnDefault("true")
    @Column(name = "la_cong_khai")
    private Boolean laCongKhai;
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ngay_tao")
    private OffsetDateTime ngayTao;

/*
 TODO [Reverse Engineering] create field to map the 'phan_loai_file' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "phan_loai_file", columnDefinition = "phan_loai_file not null")
    private Object phanLoaiFile;
*/
}