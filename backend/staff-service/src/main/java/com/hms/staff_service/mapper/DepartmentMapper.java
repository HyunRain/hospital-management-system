package com.hms.staff_service.mapper;

import com.hms.staff_service.dto.department.DepartmentRequestDto;
import com.hms.staff_service.dto.department.DepartmentResponseDto;
import com.hms.staff_service.model.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentResponseDto entityToDto(Department department) {
        return DepartmentResponseDto.builder()
                .name(department.getName())
                .staffList(department.getStaffList())
                .build();
    }

    public Department dtoToEntity(DepartmentRequestDto departmentRequestDto) {
        return Department.builder()
                .name(departmentRequestDto.getName())
                .build();
    }
}
