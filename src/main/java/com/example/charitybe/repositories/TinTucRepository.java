package com.example.charitybe.repositories;

import com.example.charitybe.entities.TinTuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TinTucRepository extends JpaRepository<TinTuc, Long> {
    List<TinTuc> findByMaTacGia(Long maTacGia);
}
