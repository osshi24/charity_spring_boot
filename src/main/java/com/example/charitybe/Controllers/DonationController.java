package com.example.charitybe.Controllers;

import com.example.charitybe.Services.donation.DonationService;
import com.example.charitybe.Services.blockchain.BlockchainService;
import com.example.charitybe.dto.ApiResponseDTO;
import com.example.charitybe.dto.donation.DonationRequestDTO;
import com.example.charitybe.dto.donation.DonationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Quyên góp:
 * - POST /api/v1/donations: tạo quyên góp (DB + on-chain nếu bật)
 * - GET  /api/v1/donations/onchain/total?maDuAn=: đọc tổng on-chain theo dự án
 */
@RestController
@RequestMapping("donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;
    private final BlockchainService blockchainService;

    /**
     * Tạo một khoản quyên góp mới.
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<DonationResponseDTO>> donate(@Valid @RequestBody DonationRequestDTO request) throws Exception {
        DonationResponseDTO responseDTO = donationService.donate(request);
        ApiResponseDTO<DonationResponseDTO> response = new ApiResponseDTO<>(200, responseDTO, "Donate success");
        return ResponseEntity.ok(response);
    }

    /**
     * Lấy tổng số tiền quyên góp lưu trên smart contract theo mã dự án.
     */
    @GetMapping("/onchain/total")
    public ResponseEntity<ApiResponseDTO<java.math.BigDecimal>> getOnchainTotal(@RequestParam("maDuAn") Long maDuAn) throws Exception {
        try {
            java.math.BigDecimal total = blockchainService.getProjectTotal(maDuAn);
            ApiResponseDTO<java.math.BigDecimal> response = new ApiResponseDTO<>(200, total, "On-chain total fetched");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException ex) {
            String msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
            if (msg.contains("disabled")) {
                ApiResponseDTO<java.math.BigDecimal> response = new ApiResponseDTO<>(200, java.math.BigDecimal.ZERO, "Blockchain disabled");
                return ResponseEntity.ok(response);
            }
            throw ex;
        }
    }
}
