package com.hms.patient_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimplePatientDto {
    @NotBlank
    private String patientId;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
