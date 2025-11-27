package com.example.charitybe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.charitybe.entities.ThongBao;

public interface NotificationRepository extends JpaRepository<ThongBao, Long> {
     
}
