package com.example.charitybe.repositories.monitoring;

import com.example.charitybe.dto.payment.QuyenGopResponseDTO;
import com.example.charitybe.enums.PhuongThucThanhToan;

public interface OrderMonitoringPort {
    // Truyền DTO để có đầy đủ thông tin giao dịch
    void recordSuccessfulDonation(QuyenGopResponseDTO response);
    // Thêm chức năng ghi nhận thất bại
    void recordFailedDonation(PhuongThucThanhToan phuongThuc, String maGiaoDich, String reason);
}