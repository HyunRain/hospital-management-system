package com.hms.staff_service.service;

import com.hms.staff_service.dto.department.DepartmentRequestDto;
import com.hms.staff_service.dto.department.DepartmentResponseDto;
import com.hms.staff_service.enums.Role;
import com.hms.staff_service.exception.DepartmentAlreadyExistsException;
import com.hms.staff_service.exception.DepartmentNotFoundException;
import com.hms.staff_service.mapper.DepartmentMapper;
import com.hms.staff_service.model.Department;
import com.hms.staff_service.repository.DepartmentRepository;
import com.hms.staff_service.repository.DepartmentStaffCount;
import com.hms.staff_service.repository.StaffRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final StaffRepository staffRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper, StaffRepository staffRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.staffRepository = staffRepository;
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
        List<Department> departments = departmentRepository.findAllWithHeads();
        Map<UUID, Integer> staffCountMap = staffRepository.countStaffPerDepartment()
                .stream()
                .collect(Collectors.toMap(
                        DepartmentStaffCount::getDepartmentId,
                        DepartmentStaffCount::getStaffCount
                ));

        return departments.stream().map(department -> {
            Integer staffCount = staffCountMap.getOrDefault(department.getUuid(), 0);
            return departmentMapper.entityToDto(department, staffCount);
        }).toList();
    }

    @Override
    public DepartmentResponseDto getDepartmentByName(String name) {
        Department department = departmentRepository.findByName(name)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with name: " + name));
        int staffCount = staffRepository.countByDepartment_Uuid(department.getUuid());
        return departmentMapper.entityToDto(department, staffCount);
    }

    @Override
    public void deleteDepartment(String name) {
        Department department = departmentRepository.findByName(name)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with name: " + name));
        departmentRepository.delete(department);
    }
}
