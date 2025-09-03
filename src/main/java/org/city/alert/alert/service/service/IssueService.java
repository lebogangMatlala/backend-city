package org.city.alert.alert.service.service;

import org.city.alert.alert.service.dto.UpdateIssueDTO;
import org.city.alert.alert.service.entity.Issue;
import org.city.alert.alert.service.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private AIService aiService;

    public Issue reportIssue(Issue issue) {
        // AI: classify issue type based on description
        String category = aiService.predictCategory(issue.getDescription());
        issue.setCategory(category);

        int priority = aiService.predictPriority(category);
        issue.setPriority(priority);

        issue.setStatus("OPEN");
        return issueRepository.save(issue);
    }

    public Issue updateIssue (Long id, UpdateIssueDTO updateRequest) {
        var issue = issueRepository.findById(id).orElseThrow();
        // apply updates if provided
        if (updateRequest.getComments() != null) {
            issue.setComments(updateRequest.getComments());
        }
        if (updateRequest.getWorkerId() != 0) {
            issue.setWorkerId(updateRequest.getWorkerId());
        }
        if (updateRequest.getStatus() != null) {
            issue.setStatus(updateRequest.getStatus());
        }
        return issueRepository.save(issue);
    }

    public List<Issue> getByWorker (Long workerId) {
        return issueRepository.findByWorkerId(workerId).orElseThrow();
    }

    public List<Issue> getAllIssues () {
        return issueRepository.findAll();
    }

    public Issue getIssue (Long id) {
        return issueRepository.findById(id).orElseThrow();
    }
}
