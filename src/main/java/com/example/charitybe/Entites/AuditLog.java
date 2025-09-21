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
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Column(name = "table_name", nullable = false, length = 100)
    private String tableName;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "old_values")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> oldValues;

    @Column(name = "new_values")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> newValues;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "ip_address")
    private InetAddress ipAddress;

    @Column(name = "user_agent", length = Integer.MAX_VALUE)
    private String userAgent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

}