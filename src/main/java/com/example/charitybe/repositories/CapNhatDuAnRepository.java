package com.example.charitybe.repositories;

import com.example.charitybe.entities.CapNhatDuAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapNhatDuAnRepository extends JpaRepository<CapNhatDuAn, Long> {
    List<CapNhatDuAn> findByMaDuAn(Long maDuAn);
}
