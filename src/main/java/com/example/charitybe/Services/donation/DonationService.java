package com.example.charitybe.Services.donation;

import com.example.charitybe.dto.donation.DonationRequestDTO;
import com.example.charitybe.dto.donation.DonationResponseDTO;
import org.springframework.modulith.NamedInterface;

/**
 * Dịch vụ nghiệp vụ Quyên góp (DB và tích hợp blockchain).
 */
@NamedInterface
public interface DonationService {
    /**
     * Tạo một khoản quyên góp mới: lưu DB và (nếu bật) ghi on-chain.
     *
     * @param request Thông tin quyên góp từ client
     * @return Thông tin phản hồi sau khi tạo quyên góp
     * @throws Exception Ném lỗi khi dự án không tồn tại hoặc ghi on-chain thất bại
     */
    DonationResponseDTO donate(DonationRequestDTO request) throws Exception;

    /**
     * Lấy tổng số tiền đã quyên góp (on-chain) cho một dự án.
     *
     * @param maDuAn Mã dự án cần đọc tổng
     * @return Tổng on-chain (BigDecimal)
     * @throws Exception Ném lỗi khi dự án không tồn tại hoặc gọi contract thất bại
     */
    java.math.BigDecimal getOnchainTotal(Long maDuAn) throws Exception;
}
