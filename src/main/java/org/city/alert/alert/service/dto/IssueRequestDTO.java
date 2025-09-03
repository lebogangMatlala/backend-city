package org.city.alert.alert.service.dto;

import lombok.Data;

@Data
public class IssueRequestDTO {
    private String title;
    private String description;
    private String imageBytes;   // base64
}
