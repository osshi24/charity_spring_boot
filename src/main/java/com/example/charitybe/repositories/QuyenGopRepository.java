package com.example.charitybe.repositories;

import com.example.charitybe.entities.QuyenGop;
import com.example.charitybe.enums.TrangThaiThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuyenGopRepository extends JpaRepository<QuyenGop, Long> {
    List<QuyenGop> findByMaDuAn(Long maDuAn);
    List<QuyenGop> findByMaNguoiDung(Long maNguoiDung);
    List<QuyenGop> findByTrangThai(TrangThaiThanhToan trangThai);
    Optional<QuyenGop> findByMaGiaoDich(String maGiaoDich);
}
