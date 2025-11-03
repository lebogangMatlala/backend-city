package org.city.alert.alert.service.rest;

import org.city.alert.alert.service.dto.AssignTechnicianDTO;
import org.city.alert.alert.service.dto.UpdateIssueDTO;
import org.city.alert.alert.service.entity.Issue;
import org.city.alert.alert.service.entity.IssueImage;
import org.city.alert.alert.service.error.ResourceNotFoundException;
import org.city.alert.alert.service.service.IssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService service;

    public IssueController(IssueService service) {
        this.service = service;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Issue> reportIssue(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Long userId,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {

        Issue issue = Issue.builder()
                .latitude(latitude)
                .longitude(longitude)
                .title(title)
                .description(description)
                .userId(userId)
                .build();

        // 🔒 Safety check
        if (issue.getImages() == null) {
            issue.setImages(new ArrayList<>());
        }

        if (image != null && !image.isEmpty()) {
            byte[] imageBytes = image.getBytes();

            // Log details about the image
            System.out.println("Uploading image: " + image.getOriginalFilename());
            System.out.println("Image content type: " + image.getContentType());
            System.out.println("Image size (bytes): " + imageBytes.length);

            IssueImage issueImage = IssueImage.builder()
                    .photo(imageBytes)
                    .photoName(image.getOriginalFilename())
                    .photoType(image.getContentType())
                    .issue(issue)
                    .build();

            issue.getImages().add(issueImage);
        }

        Issue response = service.reportIssue(issue);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssue(@PathVariable Long id) {
        Issue issue = service.getIssue(id);
        if (issue == null) {
            throw new ResourceNotFoundException("Issue with ID " + id + " not found");
        }
        return ResponseEntity.ok(issue);
    }

    @GetMapping("/worker/{id}")
    public ResponseEntity<List<Issue>> getIssuesByWorker(@PathVariable Long id) {
        List<Issue> issues = service.getByWorker(id);
        if (issues.isEmpty()) {
            throw new ResourceNotFoundException("No issues found for worker ID " + id);
        }
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/user/getIssuesByUser/{id}")
    public ResponseEntity<List<Issue>> getIssuesByUser(@PathVariable Long id) {
        List<Issue> issues = service.getByUser(id);
        if (issues.isEmpty()) {
            throw new ResourceNotFoundException("No issues found for user ID " + id);
        }
        return ResponseEntity.ok(issues);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Issue> updateIssue(
            @PathVariable Long id,
            @RequestBody UpdateIssueDTO updateIssueDTO
    ) {
        Issue issue = service.updateIssue(id, updateIssueDTO);
        if (issue == null) {
            throw new ResourceNotFoundException("Issue with ID " + id + " not found");
        }
        return ResponseEntity.ok(issue);
    }

    @GetMapping("/reported-by/{userId}")
    public ResponseEntity<List<Issue>> getIssuesByReporter(@PathVariable Long userId) {

        List<Issue> issues = service.getIssuesReportedByUser(userId);
        return ResponseEntity.ok(issues);
    }

    // Manual assignment by admin
    @PostMapping("/assign")
    public ResponseEntity<Issue> assignManually(@RequestBody AssignTechnicianDTO dto) {
        Issue assigned = service.assignTechnicianManually(dto);
        return ResponseEntity.ok(assigned);
    }

    @GetMapping("/getAllIssues")
    public ResponseEntity<List<Issue>> getAllIssues() {
        return ResponseEntity.ok(service.getAllIssues());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Issue> updateIssue(@PathVariable Long id, @RequestBody Issue updatedIssue) {
        return ResponseEntity.ok(service.updateIssue(id, updatedIssue));
    }
    @GetMapping("/worker/{id}/summary")
    public ResponseEntity<Map<String, Object>> getWorkerSummary(@PathVariable Long id) {
        Map<String, Object> summary = service.getWorkerSummary(id);
        return ResponseEntity.ok(summary);
    }

    @DeleteMapping("/softDelete/{id}")
    public ResponseEntity<String> softDeleteIssue(@PathVariable Long id) {
        return ResponseEntity.ok(service.softDeleteIssue(id));
    }
}
