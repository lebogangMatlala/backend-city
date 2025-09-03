package org.city.alert.alert.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueResponse {

    private Long id;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private byte[] image;   // image stored as raw bytes
    private String status;
    private LocalDateTime reportedAt;
}
