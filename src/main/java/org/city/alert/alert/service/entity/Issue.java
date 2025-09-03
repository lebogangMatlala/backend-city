package org.city.alert.alert.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "issues")
@AllArgsConstructor
@NoArgsConstructor
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status; // OPEN, IN_PROGRESS, RESOLVED

    private Double latitude;
    private Double longitude;

    // Multimedia links (S3, Local, or BLOB ids)
    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] photo;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] video;

    private Long workerId;

    // (Optional) AI-predicted category
    private String category;

    private String comments;

    private Integer priority;

    @CreationTimestamp
    private LocalDateTime reportedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
