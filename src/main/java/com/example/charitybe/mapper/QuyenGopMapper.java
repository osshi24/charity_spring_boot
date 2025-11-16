package com.example.charitybe.mapper;

import org.springframework.stereotype.Component;

import com.example.charitybe.dto.payment.QuyenGopRequestDTO;
import com.example.charitybe.dto.payment.QuyenGopResponseDTO;
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
        dto.setPhuongThucThanhToan(quyenGop.getPhuongThucThanhToan().getValue());
        dto.setPhuongThucThanhToan(quyenGop.getTrangThai().getValue());
        dto.setTrangThaiThanhToan(quyenGop.getTrangThai());
        dto.setMaGiaoDich(quyenGop.getMaGiaoDich());
        dto.setLoiNhan(quyenGop.getLoiNhan());
        dto.setSoTienThuc(quyenGop.getSoTienThuc());
        return dto;
    }

    public QuyenGop toEntity(QuyenGopRequestDTO request) {
        QuyenGop payment = new QuyenGop();
        payment.setMaNguoiDung(request.getMaNguoiDung());
        payment.setMaDuAn(request.getMaDuAn());
        payment.setSoTien(request.getSoTien());
        // Bạn cần chuyển đổi từ String sang enum cho các trường enum
        if (request.getPhuongThucThanhToan() != null) {
            payment.setPhuongThucThanhToan(PhuongThucThanhToan.valueOf(request.getPhuongThucThanhToan()));
        }
        if (request.getTrangThaiThanhToan() != null) {
            payment.setTrangThai(TrangThaiThanhToan.valueOf(request.getTrangThaiThanhToan()));
        }
        payment.setSoTienThuc(request.getSoTienThuc());
        payment.setDonViTienTe(request.getDonViTienTe());
        payment.setMaGiaoDich(request.getMaGiaoDich());
        payment.setLoiNhan(request.getLoiNhan());
        return payment;
    }
}
