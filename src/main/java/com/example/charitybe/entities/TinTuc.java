package com.example.charitybe.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tin_tuc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TinTuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    @Column(name = "tieu_de", nullable = false, length = 255)
    private String tieuDe;

    @NotBlank(message = "Nội dung không được để trống")
    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "ma_tac_gia")
    private Long maTacGia;

    @Size(max = 500, message = "Ảnh đại diện không được vượt quá 500 ký tự")
    @Column(name = "anh_dai_dien", length = 500)
    private String anhDaiDien;

    @Column(name = "ngay_tao", columnDefinition = "TIMESTAMPTZ", updatable = false)
    private OffsetDateTime ngayTao = OffsetDateTime.now();

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_tac_gia", referencedColumnName = "id", insertable = false, updatable = false)
    private NguoiDung tacGia;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = OffsetDateTime.now();
        }
    }
}
