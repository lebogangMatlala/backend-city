package org.city.alert.alert.service.dto;

import org.city.alert.alert.service.enums.Department;
import org.city.alert.alert.service.enums.Status;

import java.time.LocalDateTime;

public record IssueReportDTO(
        Long id,
        String title,
        String description,
        Status status,
        LocalDateTime reportedAt,
        LocalDateTime updatedAt,
        Long workerId,
        String workerName,
        Department department
) {}


