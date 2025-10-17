package com.example.charitybe.Services.blockchain;

import java.math.BigDecimal;

/**
 * Dịch vụ tương tác với Blockchain để ghi/đọc dữ liệu quyên góp.
 */
public interface BlockchainService {
    /**
     * Ghi thông tin quyên góp lên smart contract.
     *
     * @param projectId            Mã dự án (projectId) trên hệ thống/contract
     * @param donorAddressOrName   Địa chỉ ví hoặc tên người quyên góp (ghi vào on-chain)
     * @param amount               Số tiền quyên góp (đơn vị tiền tệ lớn, sẽ chuyển sang cents khi gửi lên chain)
     * @param currency             Mã tiền tệ (ví dụ: VND)
     * @param memo                 Ghi chú đi kèm giao dịch
     * @return Mã giao dịch (transaction hash) nếu gửi thành công; null nếu tính năng blockchain đang tắt
     * @throws Exception Ném lỗi khi thiếu cấu hình hoặc gửi giao dịch thất bại
     */
    String recordDonation(Long projectId,
                          String donorAddressOrName,
                          BigDecimal amount,
                          String currency,
                          String memo) throws Exception;

    /**
     * Đọc tổng số tiền quyên góp của một dự án từ smart contract.
     * Hàm trên-chain dự kiến trả về số nguyên theo đơn vị "cents"; kết quả sẽ được chuyển thành BigDecimal.
     *
     * @param projectId Mã dự án cần đọc tổng
     * @return Tổng số tiền (đơn vị tiền tệ lớn), đã chuyển đổi từ cents
     * @throws Exception Ném lỗi khi thiếu cấu hình hoặc gọi contract thất bại
     */
    BigDecimal getProjectTotal(Long projectId) throws Exception;
}
