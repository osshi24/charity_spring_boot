package com.example.charitybe.repositories;

import com.example.charitybe.entities.DangKySuKien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DangKySuKienRepository extends JpaRepository<DangKySuKien, Long> {
    List<DangKySuKien> findByMaSuKien(Long maSuKien);
    List<DangKySuKien> findByMaNguoiDung(Long maNguoiDung);
}
