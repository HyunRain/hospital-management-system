package com.hms.api_gateway.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

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
