package com.example.charitybe.Services.payment.strategies;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.example.charitybe.Config.VnPayConfig;
import com.example.charitybe.dto.payment.QuyenGopRequestVnpayDTO;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class VnPayPaymentStrategy implements PaymentStrategy {

    private final VnPayConfig vnPayConfig;

    @Override
    public boolean supports(String phuongThucThanhToan) {
        // Phương thức thanh toán cần phải là "VNPAY"
        return "VNPAY".equalsIgnoreCase(phuongThucThanhToan);
    }

    @Override
    public String createPaymentUrl(HttpServletRequest request, QuyenGopRequestVnpayDTO quyenGopRequestVnpayDTO) throws Exception {
        
        String ngayTao = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String maDonHang = new SimpleDateFormat("ddHHmmss").format(new Date());
        String diaChiIp = layDiaChiIp(request);

        Map<String, String> thamSoVnpay = new TreeMap<>();
        // ... (Đặt các tham số VNPay như code gốc của bạn)
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

        StringBuilder chuoiQuery = new StringBuilder();
        for (Map.Entry<String, String> entry : thamSoVnpay.entrySet()) {
            if (chuoiQuery.length() > 0) {
                chuoiQuery.append("&");
            }
            chuoiQuery.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()))
                     .append("=")
                     .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
        }

        String duLieuKy = chuoiQuery.toString();
        String chuKy = taoChuKyHmacSha512(vnPayConfig.getHashSecret(), duLieuKy);
        chuoiQuery.append("&vnp_SecureHash=").append(chuKy);

        return vnPayConfig.getUrl() + "?" + chuoiQuery;
    }

    // Giữ nguyên các hàm private hỗ trợ
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