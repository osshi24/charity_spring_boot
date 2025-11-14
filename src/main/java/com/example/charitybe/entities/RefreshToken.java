package com.example.charitybe.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Token không được để trống")
    @Column(name = "token", unique = true, nullable = false, length = 500)
    private String token;

    @NotNull(message = "User ID không được để trống")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "Ngày hết hạn không được để trống")
    @Column(name = "expiry_date", nullable = false)
    private OffsetDateTime expiryDate;

    @Column(name = "revoked")
    private Boolean revoked = false;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NguoiDung nguoiDung;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
        if (revoked == null) {
            revoked = false;
        }
    }

    public boolean isExpired() {
        return OffsetDateTime.now().isAfter(expiryDate);
    }
}
