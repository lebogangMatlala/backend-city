package org.city.alert.alert.service.service;

import org.city.alert.alert.service.dto.*;
import org.city.alert.alert.service.entity.Issue;

import java.util.List;

public interface ReportsService {

    List<StatusCountDTO> getStatusCounts();
    AdminSummaryReportDTO getAdminSummary();

    List<IssueReportDTO> getAllIssues();

    List<TechnicianWorkloadDTO> getTechnicianWorkload();

    List<IssuesByDepartmentDTO> getIssuesByDepartment();
}
