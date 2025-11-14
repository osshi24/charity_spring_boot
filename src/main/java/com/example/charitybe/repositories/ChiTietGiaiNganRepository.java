package com.example.charitybe.repositories;

import com.example.charitybe.entities.ChiTietGiaiNgan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietGiaiNganRepository extends JpaRepository<ChiTietGiaiNgan, Long> {
    List<ChiTietGiaiNgan> findByMaGiaiNgan(Long maGiaiNgan);
}
