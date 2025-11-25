package com.example.charitybe.Services.payment.strategies;
import com.example.charitybe.dto.payment.QuyenGopRequestVnpayDTO;

import jakarta.servlet.http.HttpServletRequest;
public interface PaymentStrategy {
  
    boolean supports(String phuongThucThanhToan);

    String createPaymentUrl(HttpServletRequest request, QuyenGopRequestVnpayDTO quyenGopRequestVnpayDTO) throws Exception;
}