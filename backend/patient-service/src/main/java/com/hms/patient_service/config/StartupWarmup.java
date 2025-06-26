package com.hms.patient_service.config;

import com.hms.patient_service.respository.PatientRepository;
import com.hms.patient_service.service.PatientService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StartupWarmup {

    private final PatientService patientService;

    public StartupWarmup(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostConstruct
    public void warmUp() {
        patientService.getAllPatientsPaginated(0,15); // or findFirstByOrderByIdAsc()
    }
}
