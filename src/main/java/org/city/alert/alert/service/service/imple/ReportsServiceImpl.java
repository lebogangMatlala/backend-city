package org.city.alert.alert.service.service.imple;

import org.city.alert.alert.service.dto.*;
import org.city.alert.alert.service.entity.Issue;
import org.city.alert.alert.service.entity.User;
import org.city.alert.alert.service.enums.Department;
import org.city.alert.alert.service.enums.Status;
import org.city.alert.alert.service.repository.IssueRepository;
import org.city.alert.alert.service.repository.UserRepository;
import org.city.alert.alert.service.service.ReportsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportsServiceImpl implements ReportsService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    public ReportsServiceImpl(IssueRepository issueRepository,UserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.userRepository= userRepository;
    }

    @Override
    public List<StatusCountDTO> getStatusCounts() {
        return issueRepository.countByStatus();
    }



    @Override
    public AdminSummaryReportDTO getAdminSummary() {
        long total = issueRepository.countAllIssues();
        long open = issueRepository.countOpenIssues();
        long inProgress = issueRepository.countInProgressIssues();
        long resolved = issueRepository.countResolvedIssues();
        Double avgSeconds = issueRepository.averageResolutionTimeSeconds();

        return new AdminSummaryReportDTO(
                total,
                open,
                inProgress,
                resolved,
                avgSeconds != null ? avgSeconds : 0.0
        );
    }

    public List<IssueReportDTO> getAllIssues() {
        return issueRepository.findAll()
                .stream()
                .map(issue -> {
                    User worker = null;
                    if (issue.getWorkerId() != null) {
                        worker = userRepository.findById(issue.getWorkerId()).orElse(null);
                    }

                    return new IssueReportDTO(
                            issue.getId(),
                            issue.getTitle(),
                            issue.getDescription(),
                            Status.valueOf(String.valueOf(issue.getStatus())), // only if issue.getStatus() is String
                            issue.getReportedAt(),
                            issue.getUpdatedAt(),
                            issue.getWorkerId(),
                            worker != null ? worker.getName() + " " + worker.getSurname() : null,
                            worker != null ? worker.getDepartment() : null // department is enum
                    );
                })
                .toList();
    }

    public List<TechnicianWorkloadDTO> getTechnicianWorkload() {
        List<User> technicians = userRepository.findAll(); // or filter only TECHNICIAN role
        List<TechnicianWorkloadDTO> result = new ArrayList<>();

        for (User tech : technicians) {
            // Count assigned and completed issues for this technician
            long assigned = issueRepository.countByWorkerId(tech.getId());
            long completed = issueRepository.countByWorkerIdAndStatus(tech.getId(), Status.RESOLVED);

            result.add(new TechnicianWorkloadDTO(
                    tech.getName() + " " + tech.getSurname(),
                    tech.getDepartment(),
                    assigned,
                    completed
            ));
        }

        return result;
    }



    public List<IssuesByDepartmentDTO> getIssuesByDepartment() {
        List<Object[]> results = issueRepository.countIssuesByDepartment();

        return results.stream()
                .map(row -> {
                    Department dept = (Department) row[0];
                    if (dept == null) {
                        dept = Department.UNASSIGNED; // optional default for unassigned
                    }

                    return new IssuesByDepartmentDTO(
                            dept,
                            ((Number) row[1]).longValue(),
                            ((Number) row[2]).longValue(),
                            ((Number) row[3]).longValue(),
                            ((Number) row[4]).longValue()
                    );
                })
                .toList();
    }



}
