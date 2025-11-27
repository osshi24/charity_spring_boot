package com.example.charitybe.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.charitybe.Services.email.EmailService;
import com.example.charitybe.Services.payment.PaymentService;
import com.example.charitybe.dto.ApiResponseDTO;
import com.example.charitybe.dto.payment.QuyenGopRequestDTO;
import com.example.charitybe.dto.payment.QuyenGopRequestVnpayDTO;
import com.example.charitybe.dto.payment.QuyenGopResponseDTO;
import com.example.charitybe.entities.QuyenGop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/quyen_gop")
@Controller
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process-vnpay")
    public ResponseEntity<Map<String, String>> taoUrlThanhToan(
            HttpServletRequest request,
            @RequestBody QuyenGopRequestVnpayDTO thanhToanRequest) {
        try {
            String urlThanhToan = paymentService.createPaymentUrl(request, thanhToanRequest);
            Map<String, String> phanHoi = new HashMap<>();
            phanHoi.put("vnpUrl", urlThanhToan);
            return ResponseEntity.ok(phanHoi);
        } catch (Exception e) {
            Map<String, String> loi = new HashMap<>();
            loi.put("message", "Không thể tạo URL thanh toán: " + e.getMessage());
            return ResponseEntity.status(500).body(loi);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<QuyenGopResponseDTO>> createPaymentForCouse(
            @Valid @RequestBody QuyenGopRequestDTO request) {
        QuyenGopResponseDTO quyenGop = paymentService.handlePayment(request, 1L);
        ApiResponseDTO<QuyenGopResponseDTO> response = new ApiResponseDTO<>(
                201, quyenGop, "Create payment successful");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

 

}