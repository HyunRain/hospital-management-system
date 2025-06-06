package com.hms.patient_service.service;


import com.hms.patient_service.dto.PatientDto;

import java.util.List;

public interface PatientService {
    PatientDto savePatient(PatientDto patientDto);
    PatientDto getPatientById(String patientId);
    List<PatientDto> getAllPatients();
    PatientDto updatePatient(String patientId, PatientDto patientDto);
    void deletePatient(String patientId);
}
