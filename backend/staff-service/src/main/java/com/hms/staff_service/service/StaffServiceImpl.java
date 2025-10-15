package com.hms.staff_service.service;


import auth.CreateUserResponse;
import auth.UserExistsResponse;
import com.hms.staff_service.dto.staff.PaginatedResponseDto;
import com.hms.staff_service.dto.staff.StaffRequestDto;
import com.hms.staff_service.dto.staff.StaffResponseDto;
import com.hms.staff_service.enums.Role;
import com.hms.staff_service.exception.EmailAlreadyExistsException;
import com.hms.staff_service.exception.PasswordsDontMatchException;
import com.hms.staff_service.exception.ResourceNotFoundException;
import com.hms.staff_service.grpc.AuthServiceGrpcClient;
import com.hms.staff_service.mapper.StaffMapper;
import com.hms.staff_service.model.Department;
import com.hms.staff_service.model.Staff;
import com.hms.staff_service.repository.DepartmentRepository;
import com.hms.staff_service.repository.StaffRepository;
import com.hms.staff_service.specifications.StaffSpecifications;
import com.hms.staff_service.util.StaffIdGenerator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final DepartmentRepository departmentRepository;
    private final AuthServiceGrpcClient authServiceGrpcClient;
    private final StaffIdGenerator staffIdGenerator;

    public StaffServiceImpl(StaffRepository staffRepository, StaffMapper staffMapper, DepartmentRepository departmentRepository, AuthServiceGrpcClient authServiceGrpcClient, StaffIdGenerator staffIdGenerator) {
        this.staffRepository = staffRepository;
        this.staffMapper = staffMapper;
        this.departmentRepository = departmentRepository;
        this.authServiceGrpcClient = authServiceGrpcClient;
        this.staffIdGenerator = staffIdGenerator;
    }

    @Override
    public StaffResponseDto registerStaff(StaffRequestDto staffRequestDto) {
        String email = staffRequestDto.getEmail();
        String password = staffRequestDto.getPassword();
        Role role = staffRequestDto.getRole();

        if (staffRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists: " + email);
        }

        Department department = departmentRepository.findByName(staffRequestDto.getDepartmentName())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + staffRequestDto.getDepartmentName()));

        if (!Objects.equals(staffRequestDto.getPassword(), staffRequestDto.getRepeatedPassword())) {
            throw new PasswordsDontMatchException("Passwords do not match");
        }

        if(authServiceGrpcClient.userExists(email).getDoesExist()) {
           throw new EmailAlreadyExistsException("User already exists: " + email);
       }
        System.out.println(role.name());
        CreateUserResponse createUserResponse = authServiceGrpcClient.createUser(email, password, role.name());
        System.out.println(createUserResponse);
        String userId = createUserResponse.getUserId();


        Staff staffToSave = staffMapper.dtoToEntity(staffRequestDto, userId);
        staffToSave.setStaffId(staffIdGenerator.generateUniqueStaffId());
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
    public PaginatedResponseDto getAllStaffPaginated(int page, int size, String role) {
        Pageable pageable = PageRequest.of(page, size);

        Page<UUID> pagedStaffIds = staffRepository.findPagedRoleIds(Role.valueOf(role), pageable);
        List<UUID> staffIds = pagedStaffIds.stream().toList();
        List<Staff> staffWithDepartment = staffRepository.findStaffWithDepartmentByIds(staffIds);
        List<StaffResponseDto> dtos = staffWithDepartment.stream().map(staffMapper::entityToDto).toList();
        return PaginatedResponseDto.builder()
                .staffResponseDtos(dtos)
                .totalPages(pagedStaffIds.getTotalPages())
                .totalStaff(pagedStaffIds.getTotalElements())
                .build();
    }

    @Override
    public PaginatedResponseDto getAllStaffPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Role> roles = List.of(Role.NURSE, Role.CLEANING_STAFF, Role.ACCOUNTANT, Role.ANESTHESIOLOGIST, Role.LAB_TECHNICIAN, Role.DIETICIAN,
                Role.PHARMACIST, Role.PHYSIOTHERAPIST, Role.RADIOLOGIST, Role.SECURITY, Role.RECEPTIONIST, Role.SURGEON);

        Page<UUID> pagedStaffIds = staffRepository.findPagedRolesIds(roles, pageable);
        List<UUID> staffIds = pagedStaffIds.stream().toList();
        List<Staff> staffWithDepartment = staffRepository.findStaffWithDepartmentByIds(staffIds);
        List<StaffResponseDto> dtos = staffWithDepartment.stream().map(staffMapper::entityToDto).toList();
        return PaginatedResponseDto.builder()
                .staffResponseDtos(dtos)
                .totalPages(pagedStaffIds.getTotalPages())
                .totalStaff(pagedStaffIds.getTotalElements())
                .build();
    }

    @Override
    public PaginatedResponseDto searchStaff(String input, Boolean doctorSearch, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        System.out.println(doctorSearch);
        Specification<Staff> spec = StaffSpecifications.staffContainsTerm(input, doctorSearch);

        Page<Staff> pagedStaff = staffRepository.findAll(spec, pageable);
        List<UUID> staffIds = pagedStaff.stream().map(Staff::getUuid).toList();

        List<Staff> allStaff = staffRepository.findStaffWithDepartmentByIds(staffIds);

        List<StaffResponseDto> dtos = allStaff.stream().map(staffMapper::entityToDto).toList();

        return PaginatedResponseDto.builder()
                .staffResponseDtos(dtos)
                .totalPages(pagedStaff.getTotalPages())
                .totalStaff(pagedStaff.getTotalElements())
                .build();
    }

    @Override
    public void deleteStaffByEmail(String email) {
        long deleteCount = staffRepository.deleteByEmail(email);
        if (deleteCount == 0) throw new ResourceNotFoundException("Staff not found with email: " + email);
    }
}
