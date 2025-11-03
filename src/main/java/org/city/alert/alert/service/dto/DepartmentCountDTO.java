package org.city.alert.alert.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.city.alert.alert.service.enums.Department;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentCountDTO {
    private Department department;
    private long count;
}
