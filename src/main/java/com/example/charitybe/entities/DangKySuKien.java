package com.example.charitybe.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "dang_ky_su_kien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DangKySuKien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ma_su_kien")
    private Long maSuKien;

    @Column(name = "ma_nguoi_dung")
    private Long maNguoiDung;

    @Size(max = 255, message = "Tên người đăng ký không được vượt quá 255 ký tự")
    @Column(name = "ten_nguoi_dang_ky", length = 255)
    private String tenNguoiDangKy;

    @Size(max = 255, message = "Email người đăng ký không được vượt quá 255 ký tự")
    @Column(name = "email_nguoi_dang_ky", length = 255)
    private String emailNguoiDangKy;

    @Column(name = "ngay_dang_ky", columnDefinition = "TIMESTAMPTZ", updatable = false)
    private OffsetDateTime ngayDangKy = OffsetDateTime.now();

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_su_kien", referencedColumnName = "id", insertable = false, updatable = false)
    private SuKien suKien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nguoi_dung", referencedColumnName = "id", insertable = false, updatable = false)
    private NguoiDung nguoiDung;

    @PrePersist
    protected void onCreate() {
        if (ngayDangKy == null) {
            ngayDangKy = OffsetDateTime.now();
        }
    }
}
