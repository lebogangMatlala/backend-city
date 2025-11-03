package org.city.alert.alert.service.rest;

import org.city.alert.alert.service.dto.*;
import org.city.alert.alert.service.service.ReportsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }



    @GetMapping("/status-counts")
    public ResponseEntity<List<StatusCountDTO>> getStatusCounts() {
        return ResponseEntity.ok(reportsService.getStatusCounts());
    }

    @GetMapping("/summary")
    public ResponseEntity<AdminSummaryReportDTO> getAdminSummary() {
        return ResponseEntity.ok(reportsService.getAdminSummary());
    }

    @GetMapping("/issues")
    public ResponseEntity<List<IssueReportDTO>> getAllIssues() {
        List<IssueReportDTO> issues = reportsService.getAllIssues();
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/workload")
    public List<TechnicianWorkloadDTO> getTechnicianWorkload() {
        return reportsService.getTechnicianWorkload();
    }
    // -----------------------------
    // Department-level issues API
    // -----------------------------
    @GetMapping("/issues-by-department")
    public List<IssuesByDepartmentDTO> getIssuesByDepartment() {
        return reportsService.getIssuesByDepartment();
    }


}
