package com.hms.staff_service.service;


import com.hms.staff_service.dto.staff.StaffRequestDto;
import com.hms.staff_service.dto.staff.StaffResponseDto;
import com.hms.staff_service.exception.EmailAlreadyExistsException;
import com.hms.staff_service.exception.ResourceNotFoundException;
import com.hms.staff_service.mapper.StaffMapper;
import com.hms.staff_service.model.Department;
import com.hms.staff_service.model.Staff;
import com.hms.staff_service.repository.DepartmentRepository;
import com.hms.staff_service.repository.StaffRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final DepartmentRepository departmentRepository;

    public StaffServiceImpl(StaffRepository staffRepository, StaffMapper staffMapper, DepartmentRepository departmentRepository) {
        this.staffRepository = staffRepository;
        this.staffMapper = staffMapper;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public StaffResponseDto registerStaff(StaffRequestDto staffRequestDto) {
        if (staffRepository.existsByEmail(staffRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + staffRequestDto.getEmail());
        }

        Department department = departmentRepository.findByName(staffRequestDto.getDepartmentName())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + staffRequestDto.getDepartmentName()));

        Staff staffToSave = staffMapper.dtoToEntity(staffRequestDto);
        staffToSave.setDepartment(department);
        Staff savedStaff = staffRepository.save(staffToSave);
        return staffMapper.entityToDto(savedStaff);
    }

    @Override
    public StaffResponseDto getStaffByEmail(String email) {
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with email: " + email));

        return staffMapper.entityToDto(staff);
    }

    @Override
    public List<StaffResponseDto> getAllStaff() {
        List<Staff> staffList = staffRepository.findAll();
        return staffList.stream().map(staffMapper::entityToDto).toList();
    }

    @Override
    public void deleteStaffByEmail(String email) {
        long deleteCount = staffRepository.deleteByEmail(email);
        if (deleteCount == 0) throw new ResourceNotFoundException("Staff not found with email: " + email);
    }
}
