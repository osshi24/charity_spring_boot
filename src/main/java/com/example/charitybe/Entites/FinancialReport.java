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
@Table(name = "financial_reports")
public class FinancialReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "report_type", nullable = false, length = 20)
    private String reportType;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "project_id")
    private Project project;

    @ColumnDefault("0.00")
    @Column(name = "total_donations", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalDonations;

    @ColumnDefault("0.00")
    @Column(name = "total_expenses", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalExpenses;

    @ColumnDefault("0.00")
    @Column(name = "administrative_costs", nullable = false, precision = 15, scale = 2)
    private BigDecimal administrativeCosts;

    @ColumnDefault("0.00")
    @Column(name = "project_expenses", nullable = false, precision = 15, scale = 2)
    private BigDecimal projectExpenses;

    @ColumnDefault("0.00")
    @Column(name = "operational_costs", nullable = false, precision = 15, scale = 2)
    private BigDecimal operationalCosts;

    @ColumnDefault("0.00")
    @Column(name = "fund_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal fundBalance;

    @Column(name = "report_file_url", length = 500)
    private String reportFileUrl;

    @ColumnDefault("'draft'")
    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "summary", length = Integer.MAX_VALUE)
    private String summary;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "published_at")
    private Instant publishedAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}