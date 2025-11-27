package com.example.charitybe.dto.payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;

import com.example.charitybe.enums.TrangThaiThanhToan;

import lombok.Data;

@Data
public class QuyenGopResponseDTO {
    private Long id;

    private Long maNguoiDung;

    private Long maDuAn;

    private BigDecimal soTien;

    private String donViTienTe;

    private String phuongThucThanhToan;

    private TrangThaiThanhToan trangThaiThanhToan;

    private String maGiaoDich;

    private String loiNhan;

    private Integer phiGiaoDich;

    private Integer soTienThuc;

    private OffsetDateTime ngayTao;

    private OffsetDateTime ngayCapNhat;

    private String blockchainTxHash;

    private Long blockchainBlockNumber;

    private String blockchainStatus;

    private OffsetDateTime blockchainTimestamp;
}
