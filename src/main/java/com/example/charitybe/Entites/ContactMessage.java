package com.example.charitybe.Entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.net.InetAddress;
import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "contact_messages")
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "message", nullable = false, length = Integer.MAX_VALUE)
    private String message;

    @ColumnDefault("'general'")
    @Column(name = "category", length = 20)
    private String category;

    @ColumnDefault("'medium'")
    @Column(name = "priority", length = 10)
    private String priority;

    @ColumnDefault("'new'")
    @Column(name = "status", length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @Column(name = "response", length = Integer.MAX_VALUE)
    private String response;

    @Column(name = "responded_at")
    private Instant respondedAt;

    @Column(name = "ip_address")
    private InetAddress ipAddress;

    @Column(name = "user_agent", length = Integer.MAX_VALUE)
    private String userAgent;

    @Column(name = "attachments")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> attachments;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}