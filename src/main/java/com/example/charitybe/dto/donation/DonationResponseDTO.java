package com.example.charitybe.dto.donation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.modulith.NamedInterface;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@NamedInterface
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonationResponseDTO {
    private Long id;
    private String maGiaoDich;
    private String maGiaoDichNgoai; // blockchain tx hash
    private BigDecimal soTien;
    private String donViTienTe;
    private String tenNguoiQuyenGop;
    private Boolean laQuyenGopAnDanh;
    private String loiNhan;
    private OffsetDateTime ngayTao;
}

