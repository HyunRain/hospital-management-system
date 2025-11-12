package com.hms.patient_service.service;


import com.hms.patient_service.dto.PaginatedResponse;
import com.hms.patient_service.dto.PatientDto;
import com.hms.patient_service.dto.SimplePatientDto;

import java.util.List;

public interface PatientService {
    PatientDto savePatient(PatientDto patientDto);
    PatientDto getPatientById(String patientId);
    PaginatedResponse getAllPatientsPaginated(int page, int size);
    List<SimplePatientDto> getPatientsByIds(List<String> patientIds);
    PaginatedResponse searchPatients(String input, int page, int size);
    PatientDto updatePatient(String patientId, PatientDto patientDto);
    void deletePatient(String patientId);
}
