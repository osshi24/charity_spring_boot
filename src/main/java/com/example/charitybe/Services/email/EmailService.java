package com.example.charitybe.Services.email;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.charitybe.dto.payment.QuyenGopResponseDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${app.url:http://localhost:5555}")
    private String appUrl;
    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Async
    public void sendDonationConfirmationEmail(
            String toEmail,
            QuyenGopResponseDTO quyenGopResponse,
            String tenNguoiDung,
            String tenDuAn) {
        
        try {
            log.info("Đang gửi email xác nhận quyên góp đến: {}", toEmail);
            

            Instant rawInstant = quyenGopResponse.getThoiGianTao(); 

            // 1. Chuyển đổi sang múi giờ Việt Nam
            ZonedDateTime vietnamTime = rawInstant.atZone(VIETNAM_ZONE);

            // 2. Định dạng thành chuỗi thân thiện
            String formattedTime = vietnamTime.format(FORMATTER);
            // Tạo context cho template
            Context context = new Context();
            context.setVariable("tenNguoiDung", tenNguoiDung);
            context.setVariable("maGiaoDich", quyenGopResponse.getMaGiaoDich());
            context.setVariable("tenDuAn", tenDuAn);
            context.setVariable("soTien", quyenGopResponse.getSoTien());
            context.setVariable("phuongThucThanhToan", quyenGopResponse.getPhuongThucThanhToan());
            System.out.println("quyenGopResponse.getPhuongThucThanhToan(): " + quyenGopResponse.getPhuongThucThanhToan());
            context.setVariable("thoiGian", formattedTime);
            context.setVariable("loiNhan", quyenGopResponse.getLoiNhan());
            context.setVariable("maDuAn", quyenGopResponse.getMaDuAn());
            context.setVariable("appName", appName);
            context.setVariable("appUrl", appUrl);

            // Render HTML template
            String htmlContent = templateEngine.process("email/donation-confirmation", context);

            // Tạo và gửi email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, appName);
            helper.setTo(toEmail);
            helper.setSubject("Xác nhận quyên góp - " + tenDuAn);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            
            log.info("Đã gửi email xác nhận quyên góp thành công đến: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Lỗi khi gửi email xác nhận quyên góp đến {}: {}", toEmail, e.getMessage(), e);
            // Không throw exception để không ảnh hưởng đến flow chính
        } catch (Exception e) {
            log.error("Lỗi không xác định khi gửi email đến {}: {}", toEmail, e.getMessage(), e);
        }
    }

    /**
     * Gửi email thông báo lỗi thanh toán
     */
    @Async
    public void sendPaymentFailureEmail(String toEmail, String tenNguoiDung, String maGiaoDich, String reason) {
        try {
            log.info("Đang gửi email thông báo lỗi thanh toán đến: {}", toEmail);

            Context context = new Context();
            context.setVariable("tenNguoiDung", tenNguoiDung);
            context.setVariable("maGiaoDich", maGiaoDich);
            context.setVariable("reason", reason);
            context.setVariable("appName", appName);
            context.setVariable("appUrl", appUrl);

            String htmlContent = templateEngine.process("email/payment-failure", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, appName);
            helper.setTo(toEmail);
            helper.setSubject("Thông báo lỗi thanh toán - " + maGiaoDich);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            
            log.info("Đã gửi email thông báo lỗi thanh toán thành công đến: {}", toEmail);

        } catch (Exception e) {
            log.error("Lỗi khi gửi email thông báo lỗi thanh toán đến {}: {}", toEmail, e.getMessage(), e);
        }
    }
}