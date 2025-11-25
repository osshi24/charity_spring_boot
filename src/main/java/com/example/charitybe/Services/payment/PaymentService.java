package com.example.charitybe.Services.payment;

import java.util.List; // Cần thêm import này

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.charitybe.Config.VnPayConfig;
import com.example.charitybe.Services.email.EmailService;
import com.example.charitybe.Services.payment.strategies.PaymentStrategy;
import com.example.charitybe.dto.payment.QuyenGopRequestDTO;
import com.example.charitybe.dto.payment.QuyenGopRequestVnpayDTO;
import com.example.charitybe.dto.payment.QuyenGopResponseDTO;
import com.example.charitybe.entities.DuAn;
import com.example.charitybe.entities.NguoiDung;
import com.example.charitybe.entities.QuyenGop;
import com.example.charitybe.enums.PhuongThucThanhToan;
import com.example.charitybe.enums.TrangThaiThanhToan;
import com.example.charitybe.mapper.QuyenGopMapper;
import com.example.charitybe.repositories.DuAnRepository;
import com.example.charitybe.repositories.NguoiDungRepository;
import com.example.charitybe.repositories.QuyenGopRepository;
import com.example.charitybe.repositories.monitoring.OrderMonitoringPort;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.micrometer.core.instrument.Timer;

@Service
@Slf4j
public class PaymentService {

    private final QuyenGopMapper quyenGopMapper;
    private final QuyenGopRepository quyenGopRepository;
    private final List<PaymentStrategy> paymentStrategies;
    private final OrderMonitoringPort orderMonitoringPort;
    private final EmailService emailService;
    private final NguoiDungRepository nguoiDungRepository;
    private final DuAnRepository duAnRepository;
    private final Timer paymentProcessingTimer;

    public PaymentService(
            QuyenGopMapper quyenGopMapper,
            QuyenGopRepository quyenGopRepository,
            List<PaymentStrategy> paymentStrategies,
            OrderMonitoringPort orderMonitoringPort,
            EmailService emailService,
            NguoiDungRepository nguoiDungRepository,
            DuAnRepository duAnRepository,
            MeterRegistry registry

    ) {
        this.quyenGopMapper = quyenGopMapper;
        this.quyenGopRepository = quyenGopRepository;
        this.paymentStrategies = paymentStrategies;
        this.orderMonitoringPort = orderMonitoringPort;
        this.emailService = emailService;
        this.nguoiDungRepository = nguoiDungRepository;
        this.duAnRepository = duAnRepository;
        // Logic khởi tạo đặc biệt: Dùng registry để đăng ký Timer
        this.paymentProcessingTimer = Timer.builder("charity.donation")
                .description("Thời gian xử lý toàn bộ quá trình handlePayment")
                .register(registry);
    }

    // Method mới, sử dụng Strategy Pattern
    public String createPaymentUrl(HttpServletRequest request, QuyenGopRequestVnpayDTO quyenGopRequestVnpayDTO)
            throws Exception {

        // 1. Tìm chiến lược phù hợp dựa trên phương thức thanh toán
        PaymentStrategy strategy = paymentStrategies.stream()
                // Giả sử DTO có field getPhuongThucThanhToan() để xác định chiến lược
                .filter(s -> s.supports(quyenGopRequestVnpayDTO.getPhuongThucThanhToan()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException(
                        "Phương thức thanh toán " + quyenGopRequestVnpayDTO.getPhuongThucThanhToan()
                                + " không được hỗ trợ."));

        // 2. Sử dụng chiến lược đó để tạo URL
        return strategy.createPaymentUrl(request, quyenGopRequestVnpayDTO);
    }

    public QuyenGopResponseDTO handlePayment(QuyenGopRequestDTO request, Long userId) {
        Timer.Sample sample = Timer.start();
        QuyenGop quyenGop = quyenGopMapper.toEntity(request);
        try {

            quyenGop.setMaNguoiDung(6L);
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
            orderMonitoringPort.recordSuccessfulDonation(response);
            sendConfirmationEmail(quyenGop, response);
            return response;
        } catch (Exception e) {
            // 🛑 GIÁM SÁT THẤT BẠI (Qua Port)
            orderMonitoringPort.recordFailedDonation(
                    quyenGop.getPhuongThucThanhToan(),
                    quyenGop.getMaGiaoDich(),
                    "DB Save Error: " + e.getMessage());
            sendFailureEmail(userId, quyenGop.getMaGiaoDich(), e.getMessage());

            // Ném lại lỗi để Controller xử lý
            throw new RuntimeException("Error handling donation: " + e.getMessage(), e);

        } finally {
            // Hoàn tất đo thời gian
            sample.stop(paymentProcessingTimer);
        }
    }



    private void sendConfirmationEmail(QuyenGop quyenGop, QuyenGopResponseDTO response) {
        try {
            // Lấy thông tin người dùng
            NguoiDung nguoiDung = nguoiDungRepository.findById(quyenGop.getMaNguoiDung())
                    .orElse(null);
            
            if (nguoiDung == null || nguoiDung.getEmail() == null) {
                log.warn("Không tìm thấy email của người dùng ID: {}", quyenGop.getMaNguoiDung());
                return;
            }

            // Lấy thông tin dự án
            DuAn duAn = duAnRepository.findById(quyenGop.getMaDuAn())
                    .orElse(null);
            
            String tenDuAn = duAn.getTieuDe();
            String tenNguoiDung =nguoiDung.getHo() + " " + nguoiDung.getTen();

            // Gửi email (async)
            emailService.sendDonationConfirmationEmail(
                    nguoiDung.getEmail(),
                    response,
                    tenNguoiDung,
                    tenDuAn
            );

            log.info("Đã gửi yêu cầu email xác nhận quyên góp cho user: {}", nguoiDung.getEmail());

        } catch (Exception e) {
            log.error("Lỗi khi gửi email xác nhận quyên góp: {}", e.getMessage(), e);
            // Không throw exception để không ảnh hưởng đến flow chính
        }
    }

    /**
     * Gửi email thông báo lỗi thanh toán
     */
    private void sendFailureEmail(Long userId, String maGiaoDich, String reason) {
        try {
            NguoiDung nguoiDung = nguoiDungRepository.findById(userId).orElse(null);
            
            if (nguoiDung == null || nguoiDung.getEmail() == null) {
                return;
            }

            String tenNguoiDung = nguoiDung.getHo() + " " + nguoiDung.getTen();

            emailService.sendPaymentFailureEmail(
                    nguoiDung.getEmail(),
                    tenNguoiDung,
                    maGiaoDich,
                    reason
            );

            log.info("Đã gửi email thông báo lỗi thanh toán cho user: {}", nguoiDung.getEmail());

        } catch (Exception e) {
            log.error("Lỗi khi gửi email thông báo lỗi: {}", e.getMessage(), e);
        }
    }

}