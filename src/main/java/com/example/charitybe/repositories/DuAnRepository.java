package com.example.charitybe.repositories;

import com.example.charitybe.entities.DuAn;
import com.example.charitybe.enums.TrangThaiDuAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DuAnRepository extends JpaRepository<DuAn, Long> {
    List<DuAn> findByTrangThai(TrangThaiDuAn trangThai);
    List<DuAn> findByNguoiTao(Long nguoiTao);
    List<DuAn> findByMaDanhMuc(Integer maDanhMuc);
}
