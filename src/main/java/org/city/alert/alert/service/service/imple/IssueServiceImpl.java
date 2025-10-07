package org.city.alert.alert.service.service.imple;

import org.city.alert.alert.service.dto.AssignTechnicianDTO;
import org.city.alert.alert.service.dto.UpdateIssueDTO;
import org.city.alert.alert.service.entity.Issue;
import org.city.alert.alert.service.entity.User;
import org.city.alert.alert.service.error.ResourceNotFoundException;
import org.city.alert.alert.service.repository.IssueRepository;
import org.city.alert.alert.service.repository.UserRepository;
import org.city.alert.alert.service.service.imple.AIService;
import org.city.alert.alert.service.service.IssueService;
import org.city.alert.alert.service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AIService aiService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Issue reportIssue(Issue issue) {
        String category = aiService.predictCategory(issue.getDescription());
        issue.setCategory(category);

        int priority = aiService.predictPriority(category);
        issue.setPriority(priority);

        issue.setStatus("OPEN");

        // cascade will also save images
        return issueRepository.save(issue);
    }

    @Override
    public Issue updateIssue(Long id, UpdateIssueDTO updateRequest) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new RuntimeException("Issue not found"));

        if (updateRequest.getComments() != null) {
            issue.setComments(updateRequest.getComments());
        }
        if (updateRequest.getWorkerId() != null && updateRequest.getWorkerId() != 0) {
            issue.setWorkerId(updateRequest.getWorkerId());
        }
        if (updateRequest.getStatus() != null) {
            issue.setStatus(updateRequest.getStatus());
        }

        return issueRepository.save(issue);
    }

    @Override
    public Issue autoAssignTechnician(Issue issue) {
        List<User> technicians = userRepository.findByRoleAndDepartment("TECHNICIAN", issue.getCategory());

        if (!technicians.isEmpty()) {
            // Pick technician with least workload, safely handling null
            User assigned = technicians.stream()
                    .min(Comparator.comparingInt(u -> u.getWorkloadCount() != null ? u.getWorkloadCount() : 0))
                    .get();

            // Assign technician
            issue.setWorkerId(assigned.getId());
            issueRepository.save(issue);

            // Update workload safely
            int currentWorkload = assigned.getWorkloadCount() != null ? assigned.getWorkloadCount() : 0;
            assigned.setWorkloadCount(currentWorkload + 1);
            userRepository.save(assigned);

            // Notify technician
            notificationService.createNotification(assigned, "New task assigned: " + issue.getTitle(), issue);

            // Notify reporter
            User reporter = userRepository.findById(issue.getUserId()).orElse(null);
            if (reporter != null) {
                notificationService.createNotification(reporter,
                        "Your issue has been assigned to " + assigned.getName(), issue);
            }
        }

        return issue;
    }

    @Override
    public Issue assignTechnicianManually(AssignTechnicianDTO dto) {

        // Validate DTO
        if (dto == null || dto.getIssueId() == null || dto.getTechnicianId() == null) {
            throw new IllegalArgumentException("Issue ID and Technician ID must not be null");
        }

        // Fetch issue
        Issue issue = issueRepository.findById(dto.getIssueId())
                .orElseThrow(() -> new ResourceNotFoundException("Issue with ID " + dto.getIssueId() + " not found"));

        // Fetch technician
        User technician = userRepository.findById(dto.getTechnicianId())
                .orElseThrow(() -> new ResourceNotFoundException("Technician with ID " + dto.getTechnicianId() + " not found"));

        // Assign technician
        issue.setWorkerId(technician.getId());
        issueRepository.save(issue);

        // Update technician workload safely
        technician.setWorkloadCount((technician.getWorkloadCount() != null ? technician.getWorkloadCount() : 0) + 1);
        userRepository.save(technician);

        // Send notification to technician
        if (technician.getId() != null) {
            notificationService.createNotification(
                    technician,
                    "New task manually assigned: " + issue.getTitle(),
                    issue
            );
        }

        // Send notification to reporter if available
        if (issue.getUserId() != null) {
            userRepository.findById(issue.getUserId())
                    .filter(u -> u.getId() != null)
                    .ifPresent(reporter -> notificationService.createNotification(
                            reporter,
                            "Your issue has been assigned to " + technician.getName(),
                            issue
                    ));
        }

        return issue;
    }


    @Override
    public Optional<List<Issue>> getByWorker(Long workerId) {
        return issueRepository.findByWorkerId(workerId); // returns List<Issue>
    }

    @Override
    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    @Override
    public Issue getIssue(Long id) {
        return issueRepository.findById(id).orElseThrow(() -> new RuntimeException("Issue not found"));
    }

    @Override
    public List<Issue> getIssuesReportedByUser(Long userId) {
        return issueRepository.findByUserId(userId);
    }
}
