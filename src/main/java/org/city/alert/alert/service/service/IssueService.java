package org.city.alert.alert.service.service;

import org.city.alert.alert.service.dto.AssignTechnicianDTO;
import org.city.alert.alert.service.dto.UpdateIssueDTO;
import org.city.alert.alert.service.entity.Issue;

import java.util.List;
import java.util.Optional;

public interface IssueService {
    Issue reportIssue(Issue issue);

    Issue updateIssue(Long id, UpdateIssueDTO updateRequest);

    Optional<List<Issue>> getByWorker(Long workerId);

    List<Issue> getAllIssues();

    Issue getIssue(Long id);

    List<Issue> getIssuesReportedByUser(Long userId);

    Issue autoAssignTechnician(Issue issue);

    // Manual assignment by admin (for /assign endpoint)
    Issue assignTechnicianManually(AssignTechnicianDTO dto);


}
