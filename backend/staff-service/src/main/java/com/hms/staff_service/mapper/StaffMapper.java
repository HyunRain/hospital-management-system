package com.hms.staff_service.mapper;

import com.hms.staff_service.dto.staff.StaffRequestDto;
import com.hms.staff_service.dto.staff.StaffResponseDto;
import com.hms.staff_service.model.Staff;
import org.springframework.stereotype.Component;

@Component
public class StaffMapper {

    public StaffResponseDto entityToDto(Staff staff) {
        return StaffResponseDto.builder()
                .userId(staff.getUserId())
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
                .postalCode(staff.getPostalCode())
                .departmentId(staff.getDepartment().getUuid())
                .build();
    }

    public Staff dtoToEntity(StaffRequestDto staffRequestDto) {
        return Staff.builder()
                .userId(staffRequestDto.getUserId())
                .firstName(staffRequestDto.getFirstName())
                .lastName(staffRequestDto.getLastName())
                .gender(staffRequestDto.getGender())
                .dateOfBirth(staffRequestDto.getDateOfBirth())
                .phoneNumber(staffRequestDto.getPhoneNumber())
                .email(staffRequestDto.getEmail())
                .addressLine1(staffRequestDto.getAddressLine1())
                .addressLine2(staffRequestDto.getAddressLine2())
                .city(staffRequestDto.getCity())
                .state(staffRequestDto.getState())
                .country(staffRequestDto.getCountry())
                .postalCode(staffRequestDto.getPostalCode())
                .build();
    }
}
