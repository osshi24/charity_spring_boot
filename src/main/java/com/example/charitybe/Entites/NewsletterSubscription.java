package com.example.charitybe.Entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "newsletter_subscriptions")
public class NewsletterSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "preferences")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> preferences;

    @ColumnDefault("'active'")
    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "source", length = 100)
    private String source;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "unsubscribed_at")
    private Instant unsubscribedAt;

    @Column(name = "last_email_sent")
    private Instant lastEmailSent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}