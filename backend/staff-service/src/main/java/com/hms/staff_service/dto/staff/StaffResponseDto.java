package com.hms.staff_service.dto.staff;


import com.hms.staff_service.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class StaffResponseDto {

    private String userId;

    private String staffId;

    private String firstName;

    private String lastName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    private String email;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private String departmentName;
}
