package org.city.alert.alert.service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.city.alert.alert.service.enums.Status;
import org.city.alert.alert.service.enums.UserRoles;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "issue_status_history")
public class IssueStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Status status; // OPEN, IN_PROGRESS, RESOLVED, CLOSED
    private String notes;  // optional comments

    private Long updatedBy; // technician/admin user ID
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;
}

