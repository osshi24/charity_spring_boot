package com.example.charitybe.repositories;

import com.example.charitybe.entities.TaiKhoanThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaiKhoanThanhToanRepository extends JpaRepository<TaiKhoanThanhToan, Long> {
    List<TaiKhoanThanhToan> findByMaDuAn(Long maDuAn);
}
