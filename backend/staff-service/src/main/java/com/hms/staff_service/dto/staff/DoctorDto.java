package com.hms.staff_service.dto.staff;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorDto {
    @NotBlank
    private String doctorId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

}
