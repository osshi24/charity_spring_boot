package com.example.charitybe.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "chi_tiet_giai_ngan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietGiaiNgan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ma_giai_ngan")
    private Long maGiaiNgan;

    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "0.0", message = "Số tiền phải >= 0")
    @Column(name = "so_tien", nullable = false, precision = 15, scale = 2)
    private BigDecimal soTien;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_giai_ngan", referencedColumnName = "id", insertable = false, updatable = false)
    private GiaiNgan giaiNgan;
}
