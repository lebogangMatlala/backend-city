package org.city.alert.alert.service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.city.alert.alert.service.enums.Department;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminIssueReportDTO {
    private Long issueId;
    private String title;
    private String status;
    private String category;
    private Department department;
    private String technicianName;
    private Integer priority;
    private Long totalStatusUpdates;
    private Long resolutionTimeSeconds;
}

