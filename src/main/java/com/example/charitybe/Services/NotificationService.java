package com.example.charitybe.Services;
import com.example.charitybe.dto.payment.PaymentEvent;
import com.example.charitybe.entities.NguoiDung;
import com.example.charitybe.entities.ThongBao;
import com.example.charitybe.repositories.NguoiDungRepository;
import com.example.charitybe.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.management.Notification;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NguoiDungRepository nguoiDungRepository;

    /**
     * Lắng nghe message từ topic "donation-confirmation-topic"
     */
    @KafkaListener(topics = "donation-confirmation-topic", groupId = "charity-notification-group")
    public void handleDonationConfirmation(PaymentEvent paymentEvent) {
        log.info("📢 Nhận được sự kiện quyên góp thành công cho người dùng ID: {}", paymentEvent.getMaNguoiDung());

        try {
            // 1. Tạo Notification Entity từ DTO
            ThongBao thongbao = new ThongBao();
            
            Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findById(paymentEvent.getMaNguoiDung());

            if (nguoiDungOpt.isEmpty()) {
                log.error("🛑 Không tìm thấy người dùng với ID: {}. Không thể tạo thông báo.", paymentEvent.getMaNguoiDung());
                // Thoát khỏi hàm nếu không tìm thấy người dùng
                return; 
            }
            
            NguoiDung nguoiDung = nguoiDungOpt.get();
            thongbao.setNguoiDung(nguoiDung);
            thongbao.setTieuDe(paymentEvent.getTieuDe());
            thongbao.setNoiDung(paymentEvent.getNoiDung());
            thongbao.setLoai(paymentEvent.getLoai());
            
            // Thiết lập mặc định
            thongbao.setDaDoc(false);
            thongbao.setNgayTao(LocalDateTime.now());
            
            // 2. Lưu vào DB
            notificationRepository.save(thongbao);
            
            log.info("✅ Đã lưu thông báo ID {} thành công cho user {}", thongbao.getId(), thongbao.getNguoiDung().getId());

        } catch (Exception e) {
            log.error("❌ Lỗi khi xử lý sự kiện Kafka và lưu DB cho user {}: {}", 
                      paymentEvent.getMaNguoiDung(), e.getMessage(), e);
        }
    }
}
