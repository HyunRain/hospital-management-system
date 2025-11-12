package com.hms.patient_service.util;

import com.hms.patient_service.respository.PatientRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PatientIdGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ID_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();
    private final PatientRepository patientRepository;

    public PatientIdGenerator(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    private String generatePatientId() {
        StringBuilder patientId = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            patientId.append(CHARACTERS.charAt(index));
        }
        return patientId.toString();
    }

    public String generateUniquePatientId() {
        String patientId;

        do {
            patientId = generatePatientId();
        } while(patientRepository.existsByPatientId(patientId));

        return patientId;
    }
}
