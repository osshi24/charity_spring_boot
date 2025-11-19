package com.example.charitybe.repositories.monitoring;

import org.springframework.stereotype.Service;

import com.example.charitybe.dto.payment.QuyenGopResponseDTO;
import com.example.charitybe.enums.PhuongThucThanhToan;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j; // Thêm logging

@Service
@Slf4j
public class OrderMonitoringImpl implements OrderMonitoringPort {

    // Khởi tạo các Counter CỐ ĐỊNH trong class
    private final Counter totalDonationSuccessCounter;
    private final Counter totalDonationFailureCounter;

    public OrderMonitoringImpl(MeterRegistry registry) {
        // 1. Counter cho THÀNH CÔNG (Được tạo MỘT LẦN)
        this.totalDonationSuccessCounter = Counter
                .builder("charity.donation.total") // Tên metric
                .description("Total number of successful donations.")
                // Tags CỐ ĐỊNH cho metric này
                .tag("status", "success")
                .register(registry);

        // 2. Counter cho THẤT BẠI (Được tạo MỘT LẦN)
        this.totalDonationFailureCounter = Counter
                .builder("charity.donation.total")
                .description("Total number of failed donations.")
                .tag("status", "failure")
                .register(registry);
    }

    @Override
    public void recordSuccessfulDonation(QuyenGopResponseDTO response) {
        totalDonationSuccessCounter.increment();

        log.info("Donation Success: ID={}, Amount={}, Method={}",
                response.getId(), response.getSoTien(), response.getPhuongThucThanhToan());

    }

    @Override
    public void recordFailedDonation(PhuongThucThanhToan phuongThuc, String maGiaoDich, String reason) {
        totalDonationFailureCounter.increment();
        log.error("Donation Failure: Method={}, TxnID={}, Reason={}",
                phuongThuc, maGiaoDich, reason);
    }
}