package com.example.charitybe.Entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "event_registrations")
public class EventRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "guest_email")
    private String guestEmail;

    @Column(name = "guest_phone", length = 20)
    private String guestPhone;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "registration_date")
    private Instant registrationDate;

    @ColumnDefault("'registered'")
    @Column(name = "status", length = 20)
    private String status;

    @ColumnDefault("'free'")
    @Column(name = "payment_status", length = 20)
    private String paymentStatus;

    @ColumnDefault("0.00")
    @Column(name = "payment_amount", precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "special_requirements", length = Integer.MAX_VALUE)
    private String specialRequirements;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    @Column(name = "check_in_time")
    private Instant checkInTime;

    @Column(name = "feedback", length = Integer.MAX_VALUE)
    private String feedback;

    @ColumnDefault("0.00")
    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;

}