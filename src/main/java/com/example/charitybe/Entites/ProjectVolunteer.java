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
@Table(name = "project_volunteers")
public class ProjectVolunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "responsibilities", length = Integer.MAX_VALUE)
    private String responsibilities;

    @ColumnDefault("0")
    @Column(name = "hours_contributed")
    private Integer hoursContributed;

    @ColumnDefault("'applied'")
    @Column(name = "status", length = 20)
    private String status;

    @ColumnDefault("0.00")
    @Column(name = "performance_rating", precision = 3, scale = 2)
    private BigDecimal performanceRating;

    @Column(name = "feedback", length = Integer.MAX_VALUE)
    private String feedback;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "joined_at")
    private Instant joinedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

}