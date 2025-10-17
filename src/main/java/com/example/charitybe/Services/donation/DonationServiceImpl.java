package com.example.charitybe.Services.donation;

import com.example.charitybe.Entites.DuAn;
import com.example.charitybe.Entites.QuyenGop;
import com.example.charitybe.Repositories.DuAnRepository;
import com.example.charitybe.Repositories.QuyenGopRepository;
import com.example.charitybe.Services.blockchain.BlockchainService;
import com.example.charitybe.dto.donation.DonationRequestDTO;
import com.example.charitybe.dto.donation.DonationResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Cài đặt DonationService:
 * - Kiểm tra dự án
 * - Lưu bản ghi QuyênGóp vào DB
 * - Ghi on-chain (nếu bật) và lưu tx hash vào trường maGiaoDichNgoai
 */
@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {
    private final QuyenGopRepository quyenGopRepository;
    private final DuAnRepository duAnRepository;
    private final BlockchainService blockchainService;

    /**
     * Tạo quyên góp mới: lưu DB và gửi giao dịch on-chain (nếu bật blockchain).
     */
    @Override
    @Transactional
    public DonationResponseDTO donate(DonationRequestDTO request) throws Exception {
        Optional<DuAn> duAnOpt = duAnRepository.findById(request.getMaDuAn());
        if (duAnOpt.isEmpty()) {
            throw new IllegalArgumentException("Dự án không tồn tại: " + request.getMaDuAn());
        }

        QuyenGop entity = new QuyenGop();
        entity.setMaDuAn(duAnOpt.get());
        entity.setSoTien(request.getSoTien());
        entity.setDonViTienTe(request.getDonViTienTe());
        entity.setTenNguoiQuyenGop(request.getTenNguoiQuyenGop());
        entity.setEmailNguoiQuyenGop(request.getEmailNguoiQuyenGop());
        entity.setSdtNguoiQuyenGop(request.getSdtNguoiQuyenGop());
        entity.setLaQuyenGopAnDanh(Boolean.TRUE.equals(request.getLaQuyenGopAnDanh()));
        entity.setLoiNhan(request.getLoiNhan());

        entity.setNgayTao(OffsetDateTime.now());
        entity.setNgayCapNhat(OffsetDateTime.now());
        entity.setPhiGiaoDich(BigDecimal.ZERO);
        entity.setSoTienThucNhan(request.getSoTien());
        entity.setMaGiaoDich("DON-" + UUID.randomUUID());

        entity = quyenGopRepository.save(entity);

        String memo = request.getLoiNhan();
        String donorIdentity = request.getDiaChiVi() != null && !request.getDiaChiVi().isBlank()
                ? request.getDiaChiVi() : request.getTenNguoiQuyenGop();
        String txHash = null;
        try {
            txHash = blockchainService.recordDonation(
                    request.getMaDuAn(),
                    donorIdentity,
                    request.getSoTien(),
                    request.getDonViTienTe(),
                    memo
            );
        } catch (Exception ex) {
            // Propagate exception to indicate on-chain recording failed
            throw ex;
        }

        if (txHash != null) {
            entity.setMaGiaoDichNgoai(txHash);
            entity.setNgayHoanThanh(OffsetDateTime.now());
            entity = quyenGopRepository.save(entity);
        }

        return new DonationResponseDTO(
                entity.getId(),
                entity.getMaGiaoDich(),
                entity.getMaGiaoDichNgoai(),
                entity.getSoTien(),
                entity.getDonViTienTe(),
                entity.getTenNguoiQuyenGop(),
                entity.getLaQuyenGopAnDanh(),
                entity.getLoiNhan(),
                entity.getNgayTao()
        );
    }

    /**
     * Đọc tổng on-chain theo mã dự án sau khi đảm bảo dự án tồn tại.
     */
    @Override
    public BigDecimal getOnchainTotal(Long maDuAn) throws Exception {
        // Validate project exists to keep API semantics consistent
        Optional<DuAn> duAnOpt = duAnRepository.findById(maDuAn);
        if (duAnOpt.isEmpty()) {
            throw new IllegalArgumentException("Dự án không tồn tại: " + maDuAn);
        }
        return blockchainService.getProjectTotal(maDuAn);
    }
}
