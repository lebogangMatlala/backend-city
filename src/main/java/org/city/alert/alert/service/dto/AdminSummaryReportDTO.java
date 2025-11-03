package org.city.alert.alert.service.dto;


public record AdminSummaryReportDTO(
        long totalIssues,
        long openIssues,
        long inProgressIssues,
        long resolvedIssues,
        double avgResolutionTimeSeconds
) {}


