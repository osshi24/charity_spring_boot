package com.example.charitybe.repositories;

import com.example.charitybe.entities.DanhMucDuAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DanhMucDuAnRepository extends JpaRepository<DanhMucDuAn, Integer> {
}
