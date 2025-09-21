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
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Column(name = "event_date", nullable = false)
    private Instant eventDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "address", length = Integer.MAX_VALUE)
    private String address;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @ColumnDefault("0")
    @Column(name = "current_participants")
    private Integer currentParticipants;

    @Column(name = "registration_deadline")
    private Instant registrationDeadline;

    @ColumnDefault("'upcoming'")
    @Column(name = "status", length = 20)
    private String status;

    @ColumnDefault("'other'")
    @Column(name = "event_type", length = 20)
    private String eventType;

    @ColumnDefault("0.00")
    @Column(name = "registration_fee", precision = 10, scale = 2)
    private BigDecimal registrationFee;

    @Column(name = "featured_image", length = 500)
    private String featuredImage;

    @Column(name = "gallery")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> gallery;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "requirements", length = Integer.MAX_VALUE)
    private String requirements;

    @Column(name = "benefits", length = Integer.MAX_VALUE)
    private String benefits;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}