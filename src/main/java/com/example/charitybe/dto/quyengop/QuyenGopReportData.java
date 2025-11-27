package com.example.charitybe.dto.quyengop;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class QuyenGopReportData {
    private String maGiaoDich;
    private String tenDuAn;
    private BigDecimal soTien;
    private String phuongThucThanhToan;
}