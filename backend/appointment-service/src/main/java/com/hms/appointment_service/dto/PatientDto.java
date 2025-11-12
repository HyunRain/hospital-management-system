package com.hms.appointment_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientDto {
    @NotBlank
    private String patientId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
