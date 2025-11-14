package com.example.charitybe.repositories;

import com.example.charitybe.entities.GiaiNgan;
import com.example.charitybe.enums.TrangThaiGiaiNgan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiaiNganRepository extends JpaRepository<GiaiNgan, Long> {
    List<GiaiNgan> findByMaDuAn(Long maDuAn);
    List<GiaiNgan> findByTrangThai(TrangThaiGiaiNgan trangThai);
    List<GiaiNgan> findByNguoiGiaiNgan(Long nguoiGiaiNgan);
}
