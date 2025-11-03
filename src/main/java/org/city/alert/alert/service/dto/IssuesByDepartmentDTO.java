package org.city.alert.alert.service.dto;

import org.city.alert.alert.service.enums.Department;

public record IssuesByDepartmentDTO(
        Department department,
        long totalIssues,
        long openIssues,
        long inProgressIssues,
        long resolvedIssues
) {}

