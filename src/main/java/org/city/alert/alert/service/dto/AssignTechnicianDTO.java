package org.city.alert.alert.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignTechnicianDTO {
    private Long issueId;
    private Long technicianId; // Admin selects technician manually
}
