package com.example.charitybe.entities;

import com.example.charitybe.enums.PhuongThucThanhToan;
import com.example.charitybe.enums.TrangThaiThanhToan;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "quyen_gop")
@EntityListeners(com.example.charitybe.listeners.QuyenGopEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuyenGop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ma_nguoi_dung")
    private Long maNguoiDung;

    @NotNull(message = "Mã dự án không được để trống")
    @Column(name = "ma_du_an", nullable = false)
    private Long maDuAn;

    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "0.0", message = "Số tiền phải >= 0")
    @Column(name = "so_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal soTien;

    @Size(max = 255, message = "Đơn vị tiền tệ không được vượt quá 255 ký tự")
    @Column(name = "don_vi_tien_te", length = 255)
    private String donViTienTe;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    @Column(name = "phuong_thuc_thanh_toan", nullable = false, columnDefinition = "phuong_thuc_thanh_toan")
    @ColumnTransformer(write = "?::phuong_thuc_thanh_toan")
    private PhuongThucThanhToan phuongThucThanhToan;

    @Column(name = "trang_thai_", columnDefinition = "trang_thai_thanh_toan")
    @ColumnTransformer(write = "?::trang_thai_thanh_toan")
    private TrangThaiThanhToan trangThai = TrangThaiThanhToan.CHO_XU_LY;

    @Size(max = 255, message = "Mã giao dịch không được vượt quá 255 ký tự")
    @Column(name = "ma_giao_dich", unique = true, length = 255)
    private String maGiaoDich;

    @Size(max = 255, message = "Lời nhắn không được vượt quá 255 ký tự")
    @Column(name = "loi_nhan", length = 255)
    private String loiNhan;

    @Column(name = "phi_giao_dich")
    private Integer phiGiaoDich;

    @Column(name = "so_tien_thuc")
    private Integer soTienThuc;

    @Column(name = "ngay_cap_nhat", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime ngayCapNhat;

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
    @JoinColumn(name = "ma_nguoi_dung", referencedColumnName = "id", insertable = false, updatable = false)
    private NguoiDung nguoiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_du_an", referencedColumnName = "id", insertable = false, updatable = false)
    private DuAn duAn;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = OffsetDateTime.now();
        }
        if (trangThai == null) {
            trangThai = TrangThaiThanhToan.CHO_XU_LY;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = OffsetDateTime.now();
    }
}
