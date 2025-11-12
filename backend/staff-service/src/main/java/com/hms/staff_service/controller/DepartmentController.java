package com.hms.staff_service.controller;

import com.hms.staff_service.dto.department.DepartmentRequestDto;
import com.hms.staff_service.dto.department.DepartmentResponseDto;
import com.hms.staff_service.model.Department;
import com.hms.staff_service.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@Tag(name = "Department", description = "API Department Service")
@Slf4j
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<String> registerDepartment(@Valid @RequestBody DepartmentRequestDto departmentRequestDto) {
        departmentService.registerDepartment(departmentRequestDto);
        return new ResponseEntity<>("Department registered successfully", HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments() {
        List<DepartmentResponseDto> departments = departmentService.getAllDepartments();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @GetMapping("/get/{name}")
    public ResponseEntity<DepartmentResponseDto> getDepartmentByName(@PathVariable String name) {
        DepartmentResponseDto department = departmentService.getDepartmentByName(name);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @DeleteMapping("delete/{name}")
    public ResponseEntity<String> deleteDepartment(@PathVariable String name) {
        departmentService.deleteDepartment(name);
        return new ResponseEntity<>("Department deleted successfully", HttpStatus.OK);
    }

}
