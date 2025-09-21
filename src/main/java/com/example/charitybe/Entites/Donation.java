package com.example.charitybe.Entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "donations")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("'VND'")
    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "payment_method", nullable = false, length = 20)
    private String paymentMethod;

    @ColumnDefault("'pending'")
    @Column(name = "payment_status", length = 20)
    private String paymentStatus;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId;

    @ColumnDefault("false")
    @Column(name = "is_recurring")
    private Boolean isRecurring;

    @Column(name = "recurring_frequency", length = 20)
    private String recurringFrequency;

    @Column(name = "next_payment_date")
    private LocalDate nextPaymentDate;

    @Column(name = "receipt_url", length = 500)
    private String receiptUrl;

    @Column(name = "donor_name")
    private String donorName;

    @Column(name = "donor_email")
    private String donorEmail;

    @Column(name = "donor_phone", length = 20)
    private String donorPhone;

    @ColumnDefault("false")
    @Column(name = "is_anonymous")
    private Boolean isAnonymous;

    @Column(name = "message", length = Integer.MAX_VALUE)
    private String message;

    @ColumnDefault("0.00")
    @Column(name = "fee_amount", precision = 10, scale = 2)
    private BigDecimal feeAmount;

    @ColumnDefault("(amount - fee_amount)")
    @Column(name = "net_amount", precision = 15, scale = 2)
    private BigDecimal netAmount;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

}