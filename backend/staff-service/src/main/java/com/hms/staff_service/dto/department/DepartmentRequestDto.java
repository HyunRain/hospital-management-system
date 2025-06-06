package com.hms.staff_service.dto.department;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentRequestDto {
    @NotBlank
    private String name;
}
