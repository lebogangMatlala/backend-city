package org.city.alert.alert.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIssueDTO {
    private String status;
    private String comments;
    private Long workerId;
}
