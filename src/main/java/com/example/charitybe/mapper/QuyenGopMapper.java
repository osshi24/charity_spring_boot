package com.example.charitybe.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.charitybe.dto.payment.QuyenGopRequestDTO;
import com.example.charitybe.dto.payment.QuyenGopResponseDTO;
import com.example.charitybe.dto.quyengop.QuyenGopReportData;
import com.example.charitybe.entities.QuyenGop;
import com.example.charitybe.enums.PhuongThucThanhToan;
import com.example.charitybe.enums.TrangThaiThanhToan;

import lombok.Data;

@Component
@Data
public class QuyenGopMapper {
    public QuyenGopResponseDTO toDTO(QuyenGop quyenGop) {
        QuyenGopResponseDTO dto = new QuyenGopResponseDTO();
        dto.setId(quyenGop.getId());
        dto.setMaNguoiDung(quyenGop.getMaNguoiDung());
        dto.setMaDuAn(quyenGop.getMaDuAn());
        dto.setSoTien(quyenGop.getSoTien());
        dto.setDonViTienTe(quyenGop.getDonViTienTe());
        dto.setPhuongThucThanhToan(quyenGop.getPhuongThucThanhToan().getValue());
        dto.setTrangThaiThanhToan(quyenGop.getTrangThai());
        dto.setMaGiaoDich(quyenGop.getMaGiaoDich());
        dto.setLoiNhan(quyenGop.getLoiNhan());
        dto.setPhiGiaoDich(quyenGop.getPhiGiaoDich());
        dto.setSoTienThuc(quyenGop.getSoTienThuc());
        dto.setNgayTao(quyenGop.getNgayTao());
        dto.setNgayCapNhat(quyenGop.getNgayCapNhat());
        dto.setBlockchainTxHash(quyenGop.getBlockchainTxHash());
        dto.setBlockchainBlockNumber(quyenGop.getBlockchainBlockNumber());
        dto.setBlockchainStatus(quyenGop.getBlockchainStatus());
        dto.setBlockchainTimestamp(quyenGop.getBlockchainTimestamp());
        return dto;
    }

    public QuyenGop toEntity(QuyenGopRequestDTO request) {
        QuyenGop payment = new QuyenGop();
        payment.setMaNguoiDung(request.getMaNguoiDung());
        payment.setMaDuAn(request.getMaDuAn());
        payment.setSoTien(request.getSoTien());
        payment.setDonViTienTe(request.getDonViTienTe());
        // Chuyển đổi từ String sang enum, hỗ trợ cả uppercase và lowercase
        if (request.getPhuongThucThanhToan() != null) {
            try {
                // Try uppercase first (enum name: VNPAY, MOMO, CHUYEN_KHOAN)
                payment.setPhuongThucThanhToan(
                    PhuongThucThanhToan.valueOf(request.getPhuongThucThanhToan().toUpperCase())
                );
            } catch (IllegalArgumentException e) {
                // If failed, try lowercase (enum value: vnpay, momo, chuyen_khoan)
                payment.setPhuongThucThanhToan(
                    PhuongThucThanhToan.fromValue(request.getPhuongThucThanhToan())
                );
            }
        }
        if (request.getTrangThaiThanhToan() != null) {
            try {
                // Try uppercase first (enum name: CHO_XU_LY, THANH_CONG, THAT_BAI)
                payment.setTrangThai(
                    TrangThaiThanhToan.valueOf(request.getTrangThaiThanhToan().toUpperCase())
                );
            } catch (IllegalArgumentException e) {
                // If failed, try lowercase (enum value: cho_xu_ly, thanh_cong, that_bai)
                payment.setTrangThai(
                    TrangThaiThanhToan.fromValue(request.getTrangThaiThanhToan())
                );
            }
        }
        payment.setMaGiaoDich(request.getMaGiaoDich());
        payment.setLoiNhan(request.getLoiNhan());
        payment.setPhiGiaoDich(request.getPhiGiaoDich());
        payment.setSoTienThuc(request.getSoTienThuc());
        return payment;
    }

    public List<QuyenGopReportData> convertToReportData(List<QuyenGop> quyenGopList) {
        return quyenGopList.stream().map(qg -> {
            QuyenGopReportData dto = new QuyenGopReportData();
            dto.setMaGiaoDich(qg.getMaGiaoDich());
            dto.setTenDuAn(qg.getDuAn().getTieuDe());
            dto.setSoTien(qg.getSoTien());
            dto.setPhuongThucThanhToan(qg.getPhuongThucThanhToan().getValue());
            return dto;
        }).toList();
    }
}
