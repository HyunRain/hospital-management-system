package com.hms.staff_service.mapper;

import com.hms.staff_service.dto.staff.StaffRequestDto;
import com.hms.staff_service.dto.staff.StaffResponseDto;
import com.hms.staff_service.model.Staff;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StaffMapper {

    public StaffResponseDto entityToDto(Staff staff) {
        return StaffResponseDto.builder()
                .userId(staff.getUserId())
                .staffId(staff.getStaffId())
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .gender(staff.getGender())
                .dateOfBirth(staff.getDateOfBirth())
                .phoneNumber(staff.getPhoneNumber())
                .email(staff.getEmail())
                .addressLine1(staff.getAddressLine1())
                .addressLine2(staff.getAddressLine2())
                .city(staff.getCity())
                .state(staff.getState())
                .country(staff.getCountry())
                .role(String.valueOf(staff.getRole()))
                .postalCode(staff.getPostalCode())
                .departmentName(staff.getDepartment().getName())
                .build();
    }

    public Staff dtoToEntity(StaffRequestDto staffRequestDto, String userId) {
        return Staff.builder()
                .userId(userId)
                .firstName(staffRequestDto.getFirstName())
                .lastName(staffRequestDto.getLastName())
                .gender(staffRequestDto.getGender())
                .dateOfBirth(staffRequestDto.getDateOfBirth())
                .phoneNumber(staffRequestDto.getPhoneNumber())
                .email(staffRequestDto.getEmail())
                .addressLine1(staffRequestDto.getAddressLine1())
                .addressLine2(staffRequestDto.getAddressLine2())
                .role(staffRequestDto.getRole())
                .city(staffRequestDto.getCity())
                .state(staffRequestDto.getState())
                .country(staffRequestDto.getCountry())
                .postalCode(staffRequestDto.getPostalCode())
                .build();
    }
}
