package com.example.charitybe.Entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "volunteers")
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "skills")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> skills;

    @Column(name = "availability")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> availability;

    @Column(name = "experience", length = Integer.MAX_VALUE)
    private String experience;

    @Column(name = "bio", length = Integer.MAX_VALUE)
    private String bio;

    @ColumnDefault("'active'")
    @Column(name = "status", length = 20)
    private String status;

    @ColumnDefault("0.00")
    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;

    @ColumnDefault("0")
    @Column(name = "total_hours")
    private Integer totalHours;

    @Column(name = "languages")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> languages;

    @Column(name = "education_level", length = 20)
    private String educationLevel;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "emergency_contact")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> emergencyContact;

    @ColumnDefault("false")
    @Column(name = "background_check")
    private Boolean backgroundCheck;

    @Column(name = "background_check_date")
    private LocalDate backgroundCheckDate;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}