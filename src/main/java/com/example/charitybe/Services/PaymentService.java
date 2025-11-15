package com.example.charitybe.Services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.example.charitybe.Config.VnPayConfig;
import com.example.charitybe.dto.payment.QuyenGopRequestDTO;
import com.example.charitybe.dto.payment.QuyenGopRequestVnpayDTO;
import com.example.charitybe.dto.payment.QuyenGopResponseDTO;
import com.example.charitybe.entities.QuyenGop;
import com.example.charitybe.enums.PhuongThucThanhToan;
import com.example.charitybe.enums.TrangThaiThanhToan;
import com.example.charitybe.mapper.QuyenGopMapper;
import com.example.charitybe.repositories.QuyenGopRepository;
import io.micrometer.common.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final QuyenGopMapper quyenGopMapper;
    private final QuyenGopRepository quyenGopRepository;
    // private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    private final VnPayConfig vnPayConfig;

    public QuyenGopResponseDTO createPaymentForCourse(QuyenGopRequestDTO request, Long userId) {
 
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
        //         payment.getId(),
        //         payment.getUserId(),
        //         payment.getCourseId(),
        //         "Thanh toán khóa học thành công");
        // kafkaTemplate.send("payment-event", paymentEvent);

        // ApiResponseDTO<UserResponseDTO> user = userClient.getUserById(userId);
        // ApiResponseDTO<CourseResponseDTO> course =
        // courseClient.getCourseById(request.getCourseId());
        QuyenGopResponseDTO response = quyenGopMapper.toDTO(quyenGop);
      
        return response;
    }



     public String taoUrlThanhToan(HttpServletRequest request, QuyenGopRequestVnpayDTO quyenGopRequestVnpayDTO) throws Exception {
        // Tạo ngày giờ hiện tại
        String ngayTao = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String maDonHang = new SimpleDateFormat("ddHHmmss").format(new Date());

        // Lấy địa chỉ IP của client
        String diaChiIp = layDiaChiIp(request);

        // Tạo các tham số VNPay
        Map<String, String> thamSoVnpay = new TreeMap<>();
        thamSoVnpay.put("vnp_Version", "2.1.0");
        thamSoVnpay.put("vnp_Command", "pay");
        thamSoVnpay.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        thamSoVnpay.put("vnp_Locale", "vn");
        thamSoVnpay.put("vnp_CurrCode", "VND");
        thamSoVnpay.put("vnp_TxnRef", maDonHang);
        thamSoVnpay.put("vnp_OrderInfo", quyenGopRequestVnpayDTO.getLoiNhan());
        thamSoVnpay.put("vnp_OrderType", "order");
        thamSoVnpay.put("vnp_Amount", String.valueOf(quyenGopRequestVnpayDTO.getSoTien() * 100));
        thamSoVnpay.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        thamSoVnpay.put("vnp_IpAddr", diaChiIp);
        thamSoVnpay.put("vnp_CreateDate", ngayTao);

        // Tạo chuỗi query
        StringBuilder chuoiQuery = new StringBuilder();
        for (Map.Entry<String, String> entry : thamSoVnpay.entrySet()) {
            if (chuoiQuery.length() > 0) {
                chuoiQuery.append("&");
            }
            chuoiQuery.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()))
                     .append("=")
                     .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
        }

        // Tạo chữ ký HMAC SHA512
        String duLieuKy = chuoiQuery.toString();
        String chuKy = taoChuKyHmacSha512(vnPayConfig.getHashSecret(), duLieuKy);
        chuoiQuery.append("&vnp_SecureHash=").append(chuKy);

        // Tạo URL thanh toán cuối cùng
        return vnPayConfig.getUrl() + "?" + chuoiQuery;
    }


    private String layDiaChiIp(HttpServletRequest request) {
        String diaChiIp = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(diaChiIp)) {
            diaChiIp = request.getRemoteAddr();
        }
        return diaChiIp;
    }

   
    private String taoChuKyHmacSha512(String khoaBiMat, String duLieu) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA512");
        SecretKeySpec khoa = new SecretKeySpec(khoaBiMat.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        mac.init(khoa);
        byte[] ketQuaKy = mac.doFinal(duLieu.getBytes(StandardCharsets.UTF_8));
        StringBuilder chuoiHex = new StringBuilder();
        for (byte b : ketQuaKy) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                chuoiHex.append('0');
            }
            chuoiHex.append(hex);
        }
        return chuoiHex.toString();
    }
}
