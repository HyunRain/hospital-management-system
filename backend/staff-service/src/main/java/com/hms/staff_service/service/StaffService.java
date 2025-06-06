package com.hms.staff_service.service;

import com.hms.staff_service.dto.staff.StaffRequestDto;
import com.hms.staff_service.dto.staff.StaffResponseDto;

import java.util.List;

public interface StaffService {
    StaffResponseDto registerStaff(StaffRequestDto staffRequestDto);

    StaffResponseDto getStaffByEmail(String email);

    List<StaffResponseDto> getAllStaff();

    void deleteStaffByEmail(String email);
}
