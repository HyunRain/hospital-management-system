package com.hms.staff_service.service;

import com.hms.staff_service.dto.staff.PaginatedResponseDto;
import com.hms.staff_service.dto.staff.StaffRequestDto;
import com.hms.staff_service.dto.staff.StaffResponseDto;

import java.util.List;

public interface StaffService {
    StaffResponseDto registerStaff(StaffRequestDto staffRequestDto);

    StaffResponseDto getStaffByEmail(String email);

    PaginatedResponseDto getAllStaffPaginated(int page, int size, String role);

    PaginatedResponseDto getAllStaffPaginated(int page, int size);

    PaginatedResponseDto searchDoctors(String input, int page, int size);

    void deleteStaffByEmail(String email);


}
