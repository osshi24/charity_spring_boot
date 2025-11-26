package com.example.charitybe.dto.payment;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.charitybe.enums.TrangThaiThanhToan;

import lombok.Data;

@Data
public class QuyenGopResponseDTO {
    private Long id;
    private Long maNguoiDung;
    
    private Long maDuAn;

    private BigDecimal soTien;
    
    private String phuongThucThanhToan;

    private TrangThaiThanhToan trangThaiThanhToan;
    private String maGiaoDich;
    private String loiNhan;
    private Integer soTienThuc;
    private Instant thoiGianTao;
}
