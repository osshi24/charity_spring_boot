package com.example.charitybe.Entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @Column(name = "excerpt", length = 500)
    private String excerpt;

    @ColumnDefault("'news'")
    @Column(name = "category", length = 20)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ColumnDefault("'draft'")
    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "featured_image", length = 500)
    private String featuredImage;

    @Column(name = "gallery")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> gallery;

    @Column(name = "tags")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> tags;

    @ColumnDefault("0")
    @Column(name = "view_count")
    private Integer viewCount;

    @ColumnDefault("false")
    @Column(name = "is_featured")
    private Boolean isFeatured;

    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "meta_description")
    private String metaDescription;

    @Column(name = "published_at")
    private Instant publishedAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}