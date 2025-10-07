package org.city.alert.alert.service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "issue_images")
public class IssueImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoName;
    private String photoType;

    // Modern Hibernate 6 mapping for PostgreSQL BYTEA
    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "photo")
    private byte[] photo;

    // 🔗 Many images belong to one issue
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    @JsonBackReference
    private Issue issue;



}
