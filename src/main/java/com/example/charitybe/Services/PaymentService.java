// package com.example.charitybe.Services;

// import java.net.URLEncoder;
// import java.nio.charset.StandardCharsets;
// import java.text.SimpleDateFormat;
// import java.util.Date;
// import java.util.Map;
// import java.util.TreeMap;

// import javax.crypto.Mac;
// import javax.crypto.spec.SecretKeySpec;

// import org.springframework.stereotype.Service;

// import com.example.charitybe.Config.VnPayConfig;
// import com.example.charitybe.dto.payment.QuyenGopRequestDTO;
// import com.example.charitybe.dto.payment.QuyenGopRequestVnpayDTO;
// import com.example.charitybe.dto.payment.QuyenGopResponseDTO;
// import com.example.charitybe.entities.QuyenGop;
// import com.example.charitybe.enums.PhuongThucThanhToan;
// import com.example.charitybe.enums.TrangThaiThanhToan;
// import com.example.charitybe.mapper.QuyenGopMapper;
// import com.example.charitybe.repositories.QuyenGopRepository;
// import io.micrometer.common.util.StringUtils;

// import jakarta.servlet.http.HttpServletRequest;
// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class PaymentService {
//     private final QuyenGopMapper quyenGopMapper;
//     private final QuyenGopRepository quyenGopRepository;
//     // private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
//     private final VnPayConfig vnPayConfig;

//     public QuyenGopResponseDTO createPaymentForCourse(QuyenGopRequestDTO request, Long userId) {

//         QuyenGop quyenGop = quyenGopMapper.toEntity(request);

//         quyenGop.setMaNguoiDung(1L);
//         quyenGop.setMaDuAn(request.getMaDuAn());
//         quyenGop.setSoTien(request.getSoTien());
//         quyenGop.setPhuongThucThanhToan(PhuongThucThanhToan.valueOf(request.getPhuongThucThanhToan()));
//         quyenGop.setTrangThai(TrangThaiThanhToan.valueOf(request.getTrangThaiThanhToan()));
//         quyenGop.setMaGiaoDich(request.getMaGiaoDich());
//         quyenGop.setLoiNhan(request.getLoiNhan());

//         quyenGop = quyenGopRepository.save(quyenGop);
//         // PaymentEvent paymentEvent = new PaymentEvent(
//         // payment.getId(),
//         // payment.getUserId(),
//         // payment.getCourseId(),
//         // "Thanh toán khóa học thành công");
//         // kafkaTemplate.send("payment-event", paymentEvent);

//         // ApiResponseDTO<UserResponseDTO> user = userClient.getUserById(userId);
//         // ApiResponseDTO<CourseResponseDTO> course =
//         // courseClient.getCourseById(request.getCourseId());
//         QuyenGopResponseDTO response = quyenGopMapper.toDTO(quyenGop);

//         return response;
//     }

// }
