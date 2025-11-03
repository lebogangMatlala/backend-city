package org.city.alert.alert.service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.city.alert.alert.service.enums.Status;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status; // OPEN, IN_PROGRESS, RESOLVED
    private Double latitude;
    private Double longitude;

    private Long workerId;   // worker assigned
    private String category; // AI-predicted category
    private String rating;
    private Integer priority;

    @CreationTimestamp
    private LocalDateTime reportedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default   // <-- REQUIRED with Lombok Builder
    @JsonManagedReference
    private List<IssueImage> images = new ArrayList<>();
    private String comments;
    private Long userId;

    // -----------------------------
    // STATUS HISTORY
    // -----------------------------
    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonManagedReference
    private List<IssueStatusHistory> statusHistory = new ArrayList<>();
    @Column(nullable = false)
    private boolean isDeleted = false;

}
