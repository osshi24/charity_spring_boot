package com.example.charitybe.entities;

import com.example.charitybe.enums.LoaiGiaiNgan;
import com.example.charitybe.enums.TrangThaiGiaiNgan;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "giai_ngan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiaiNgan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ma_du_an")
    private Long maDuAn;

    @Column(name = "ma_tai_khoan_du_an")
    private Long maTaiKhoanDuAn;

    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "0.0", message = "Số tiền phải >= 0")
    @Column(name = "so_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal soTien;

    @NotNull(message = "Loại giải ngân không được để trống")
    @Column(name = "loai_giai_ngan", nullable = false, columnDefinition = "loai_giai_ngan")
    @ColumnTransformer(write = "?::loai_giai_ngan")
    private LoaiGiaiNgan loaiGiaiNgan;

    @NotNull(message = "Ngày giải ngân không được để trống")
    @Column(name = "ngay_giai_ngan", nullable = false)
    private LocalDate ngayGiaiNgan;

    @NotBlank(message = "Người nhận không được để trống")
    @Size(max = 255, message = "Người nhận không được vượt quá 255 ký tự")
    @Column(name = "nguoi_nhan", nullable = false, length = 255)
    private String nguoiNhan;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "thong_tin_nguoi_nhan", columnDefinition = "jsonb")
    private String thongTinNguoiNhan;

    @NotBlank(message = "Mục đích sử dụng không được để trống")
    @Column(name = "muc_dich_su_dung", nullable = false, columnDefinition = "TEXT")
    private String mucDichSuDung;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tai_lieu_chung_minh", columnDefinition = "jsonb")
    private String taiLieuChungMinh;

    @Column(name = "nguoi_giai_ngan")
    private Long nguoiGiaiNgan;

    @Column(name = "nguoi_duyet")
    private Long nguoiDuyet;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "trang_thai", columnDefinition = "trang_thai_giai_ngan")
    @ColumnTransformer(write = "?::trang_thai_giai_ngan")
    private TrangThaiGiaiNgan trangThai = TrangThaiGiaiNgan.CHO_DUYET;

    @Column(name = "ngay_tao", columnDefinition = "TIMESTAMPTZ", updatable = false)
    private OffsetDateTime ngayTao = OffsetDateTime.now();

    // Blockchain fields
    @Column(name = "blockchain_tx_hash", length = 66)
    private String blockchainTxHash;

    @Column(name = "blockchain_block_number")
    private Long blockchainBlockNumber;

    @Column(name = "blockchain_timestamp", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime blockchainTimestamp;

    @Column(name = "blockchain_status", length = 50)
    private String blockchainStatus; // PENDING, CONFIRMED, FAILED

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_du_an", referencedColumnName = "id", insertable = false, updatable = false)
    private DuAn duAn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_tai_khoan_du_an", referencedColumnName = "id", insertable = false, updatable = false)
    private TaiKhoanThanhToan taiKhoanThanhToan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_giai_ngan", referencedColumnName = "id", insertable = false, updatable = false)
    private NguoiDung nguoiGiaiNganEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_duyet", referencedColumnName = "id", insertable = false, updatable = false)
    private NguoiDung nguoiDuyetEntity;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = OffsetDateTime.now();
        }
        if (trangThai == null) {
            trangThai = TrangThaiGiaiNgan.CHO_DUYET;
        }
    }
}
