package com.example.charitybe.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.charitybe.Services.PaymentService;
import com.example.charitybe.dto.payment.QuyenGopRequestVnpayDTO;
import com.example.charitybe.entities.QuyenGop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Controller
public class PaymentController {

    private final PaymentService paymentService;

   @PostMapping("/process-vnpay")
    public ResponseEntity<Map<String, String>> taoUrlThanhToan(
            HttpServletRequest request,
            @RequestBody QuyenGopRequestVnpayDTO thanhToanRequest) {
        try {
            String urlThanhToan = paymentService.taoUrlThanhToan(request, thanhToanRequest);
            Map<String, String> phanHoi = new HashMap<>();
            phanHoi.put("vnpUrl", urlThanhToan);
            return ResponseEntity.ok(phanHoi);
        } catch (Exception e) {
            Map<String, String> loi = new HashMap<>();
            loi.put("message", "Không thể tạo URL thanh toán: " + e.getMessage());
            return ResponseEntity.status(500).body(loi);
        }
    }
}