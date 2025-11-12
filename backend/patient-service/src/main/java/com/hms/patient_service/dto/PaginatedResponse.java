package com.hms.patient_service.dto;

import com.hms.patient_service.model.Patient;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedResponse {
    private List<PatientDto> patients;
    private int totalPages;
    private long totalPatients;
}
