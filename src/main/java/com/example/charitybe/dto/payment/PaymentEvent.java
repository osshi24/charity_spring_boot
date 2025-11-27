package com.example.charitybe.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private Long maNguoiDung;
    private String tieuDe;
    private String noiDung;
    private String Loai;
}
