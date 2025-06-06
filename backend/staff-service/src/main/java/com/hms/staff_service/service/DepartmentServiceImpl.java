package com.hms.staff_service.service;

import com.hms.staff_service.dto.department.DepartmentRequestDto;
import com.hms.staff_service.dto.department.DepartmentResponseDto;
import com.hms.staff_service.exception.DepartmentAlreadyExistsException;
import com.hms.staff_service.exception.DepartmentNotFoundException;
import com.hms.staff_service.mapper.DepartmentMapper;
import com.hms.staff_service.model.Department;
import com.hms.staff_service.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public void registerDepartment(DepartmentRequestDto departmentRequestDto) {
        if(departmentRepository.existsByName(departmentRequestDto.getName())) {
            throw new DepartmentAlreadyExistsException("Department with name " + departmentRequestDto.getName() + " already exists.");
        }

        Department department = departmentMapper.dtoToEntity(departmentRequestDto);
        departmentRepository.save(department);
    }

    @Override
    public List<DepartmentResponseDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(departmentMapper::entityToDto).toList();
    }

    @Override
    public DepartmentResponseDto getDepartmentByName(String name) {
        Department department = departmentRepository.findByName(name)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with name: " + name));
        return departmentMapper.entityToDto(department);
    }

    @Override
    public void deleteDepartment(String name) {
        Department department = departmentRepository.findByName(name)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with name: " + name));
        departmentRepository.delete(department);
    }
}
