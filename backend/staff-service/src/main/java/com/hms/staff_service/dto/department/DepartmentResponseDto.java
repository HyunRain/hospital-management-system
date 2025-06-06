package com.hms.staff_service.dto.department;

import com.hms.staff_service.model.Staff;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DepartmentResponseDto {
    private String name;
    private List<Staff> staffList;
}
