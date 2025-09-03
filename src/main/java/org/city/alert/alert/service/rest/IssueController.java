package org.city.alert.alert.service.rest;

import org.city.alert.alert.service.dto.IssueResponse;
import org.city.alert.alert.service.dto.UpdateIssueDTO;
import org.city.alert.alert.service.entity.Issue;
import org.city.alert.alert.service.error.ResourceNotFoundException;
import org.city.alert.alert.service.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    @Autowired
    private IssueService service;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Issue> reportIssue(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam MultipartFile image
    ) throws IOException {

        // get raw bytes
        byte[] imageBytes = image.getBytes();

        var issue = Issue.builder()
                .latitude(latitude)
                .longitude(longitude)
                .photo(imageBytes)
                .title(title)
                .description(description)
                .build();

        var response = service.reportIssue(issue);

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
    public ResponseEntity<List<Issue>> getIssueByWorker(@PathVariable Long id) {
        var issues = service.getByWorker(id);

        if (issues == null) {
            throw new ResourceNotFoundException("Issue with ID " + id + " not found");
        }

        return ResponseEntity.ok(issues);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Issue> updateIssue(@PathVariable Long id,
                                                     @RequestBody UpdateIssueDTO updateIssueDTO) {
        Issue issue = service.updateIssue(id, updateIssueDTO);
        if (issue == null) {
            throw new ResourceNotFoundException("Issue with ID " + id + " not found");
        }
        return ResponseEntity.ok(issue);
    }

    @GetMapping
    public ResponseEntity<List<Issue>> getAllIssues() {
        return ResponseEntity.ok(new ArrayList<>(service.getAllIssues()));
    }
}
