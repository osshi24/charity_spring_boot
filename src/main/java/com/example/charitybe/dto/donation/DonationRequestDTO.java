package com.example.charitybe.dto.donation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonationRequestDTO {
    @NotNull
    private Long maDuAn;

    @NotNull
    private BigDecimal soTien;

    private String donViTienTe = "VND";

    private String tenNguoiQuyenGop;
    private String emailNguoiQuyenGop;
    private String sdtNguoiQuyenGop;
    private Boolean laQuyenGopAnDanh = false;
    private String loiNhan;

    // Optional blockchain donor address or identifier
    private String diaChiVi;
}

