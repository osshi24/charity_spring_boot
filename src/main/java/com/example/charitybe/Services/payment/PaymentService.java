package com.example.charitybe.Services.payment;
import java.util.List; // Cần thêm import này

import org.springframework.stereotype.Service;

import com.example.charitybe.Config.VnPayConfig;
import com.example.charitybe.Services.payment.strategies.PaymentStrategy;
import com.example.charitybe.dto.payment.QuyenGopRequestDTO;
import com.example.charitybe.dto.payment.QuyenGopRequestVnpayDTO;
import com.example.charitybe.dto.payment.QuyenGopResponseDTO;
import com.example.charitybe.entities.QuyenGop;
import com.example.charitybe.enums.PhuongThucThanhToan;
import com.example.charitybe.enums.TrangThaiThanhToan;
import com.example.charitybe.mapper.QuyenGopMapper;
import com.example.charitybe.repositories.QuyenGopRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final QuyenGopMapper quyenGopMapper;
    private final QuyenGopRepository quyenGopRepository;
    private final VnPayConfig vnPayConfig; // Vẫn giữ nếu cần dùng cho các nghiệp vụ khác

    // Inject TẤT CẢ các implementation của PaymentStrategy
    private final List<PaymentStrategy> paymentStrategies; 


    // Method mới, sử dụng Strategy Pattern
    public String createPaymentUrl(HttpServletRequest request, QuyenGopRequestVnpayDTO quyenGopRequestVnpayDTO) throws Exception {
        
        // 1. Tìm chiến lược phù hợp dựa trên phương thức thanh toán
        PaymentStrategy strategy = paymentStrategies.stream()
            // Giả sử DTO có field getPhuongThucThanhToan() để xác định chiến lược
            .filter(s -> s.supports(quyenGopRequestVnpayDTO.getPhuongThucThanhToan())) 
            .findFirst()
            .orElseThrow(() -> new UnsupportedOperationException(
                "Phương thức thanh toán " + quyenGopRequestVnpayDTO.getPhuongThucThanhToan() + " không được hỗ trợ."
            ));

        // 2. Sử dụng chiến lược đó để tạo URL
        return strategy.createPaymentUrl(request, quyenGopRequestVnpayDTO);
    }
    

      public QuyenGopResponseDTO handlePayment(QuyenGopRequestDTO request, Long userId) {

        QuyenGop quyenGop = quyenGopMapper.toEntity(request);

        quyenGop.setMaNguoiDung(1L);
        quyenGop.setMaDuAn(request.getMaDuAn());
        quyenGop.setSoTien(request.getSoTien());
        quyenGop.setPhuongThucThanhToan(PhuongThucThanhToan.valueOf(request.getPhuongThucThanhToan()));
        quyenGop.setTrangThai(TrangThaiThanhToan.valueOf(request.getTrangThaiThanhToan()));
        quyenGop.setMaGiaoDich(request.getMaGiaoDich());
        quyenGop.setLoiNhan(request.getLoiNhan());

        quyenGop = quyenGopRepository.save(quyenGop);
        // PaymentEvent paymentEvent = new PaymentEvent(
        // payment.getId(),
        // payment.getUserId(),
        // payment.getCourseId(),
        // "Thanh toán khóa học thành công");
        // kafkaTemplate.send("payment-event", paymentEvent);

        // ApiResponseDTO<UserResponseDTO> user = userClient.getUserById(userId);
        // ApiResponseDTO<CourseResponseDTO> course =
        // courseClient.getCourseById(request.getCourseId());
        QuyenGopResponseDTO response = quyenGopMapper.toDTO(quyenGop);

        return response;
    }
}