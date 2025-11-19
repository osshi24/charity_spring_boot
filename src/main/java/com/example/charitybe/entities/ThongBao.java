package com.example.charitybe.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "thong_bao")
@Data
public class ThongBao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    private NguoiDung nguoiDung;

    @Column(name = "tieu_de", length = 255)
    private String tieuDe;

    @Column(name = "loai", length = 50)
    private String loai;

    @Column(name = "da_doc", nullable = false)
    private boolean daDoc = false;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDateTime ngayTao;
}
