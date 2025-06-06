package com.hms.staff_service.service;

import com.hms.staff_service.dto.department.DepartmentRequestDto;
import com.hms.staff_service.dto.department.DepartmentResponseDto;

import java.util.List;

public interface DepartmentService {
    void registerDepartment(DepartmentRequestDto departmentRequestDto);

    List<DepartmentResponseDto> getAllDepartments();

    DepartmentResponseDto getDepartmentByName(String name);

    void deleteDepartment(String name);
}
