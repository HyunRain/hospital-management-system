package com.hms.staff_service.dto.department;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class DepartmentResponseDto {
    private String name;
    private String headOfDepartmentName;
    private UUID headOfDepartmentUuid;
    private Integer staffCount;
    private Integer bedCapacity;
    private Integer currentBedCount;
    private Boolean isActive;
}
