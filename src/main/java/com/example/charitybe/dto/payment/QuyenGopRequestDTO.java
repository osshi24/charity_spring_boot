package com.example.charitybe.dto.payment;

import java.math.BigDecimal;

import com.example.charitybe.enums.TrangThaiThanhToan;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuyenGopRequestDTO {

    @NotNull(message = "User ID cannot be null")
    private Long maNguoiDung;

    @NotNull(message = "Project ID cannot be null")
    private Long maDuAn;

    @NotNull(message = "Amount cannot be null")
    private BigDecimal soTien;
    
    private String phuongThucThanhToan;

    private String trangThaiThanhToan;
    private String maGiaoDich;
    private String donViTienTe;
    private String loiNhan;
    private Integer soTienThuc;
}
