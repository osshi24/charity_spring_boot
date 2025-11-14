package com.example.charitybe.repositories;

import com.example.charitybe.entities.SuKien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuKienRepository extends JpaRepository<SuKien, Long> {
    List<SuKien> findByNguoiTao(Long nguoiTao);
}
