package com.hms.staff_service.mapper;

import com.hms.staff_service.dto.department.DepartmentRequestDto;
import com.hms.staff_service.dto.department.DepartmentResponseDto;
import com.hms.staff_service.model.Department;
import com.hms.staff_service.model.Staff;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DepartmentMapper {

    public DepartmentResponseDto entityToDto(Department department, Integer staffCount) {
        String fullName = null;
        UUID headOfDepartmentUuid = null;

        Staff headOfDepartment = department.getHeadOfDepartment();

        if(headOfDepartment != null) {
            fullName = headOfDepartment.getFirstName() + " " + headOfDepartment.getLastName();
            headOfDepartmentUuid = headOfDepartment.getUuid();
        }

        return DepartmentResponseDto.builder()
                .name(department.getName())
                .headOfDepartmentName(fullName)
                .headOfDepartmentUuid(headOfDepartmentUuid)
                .staffCount(staffCount)
                .bedCapacity(department.getBedCapacity())
                .currentBedCount(department.getCurrentBedCount())
                .isActive(department.getIsActive())
                .build();
    }

    public Department dtoToEntity(DepartmentRequestDto departmentRequestDto) {
        return Department.builder()
                .name(departmentRequestDto.getName())
                .build();
    }
}
