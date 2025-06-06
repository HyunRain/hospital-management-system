package com.hms.patient_service.service;

import billing.BillingResponse;
import com.hms.patient_service.exception.EmailAlreadyExistsException;
import com.hms.patient_service.exception.PhoneNumberAlreadyExistsException;
import com.hms.patient_service.exception.ResourceNotFoundException;
import com.hms.patient_service.dto.PatientDto;
import com.hms.patient_service.grpc.BillingServiceGrpcClient;
import com.hms.patient_service.mapper.PatientMapper;
import com.hms.patient_service.model.Patient;
import com.hms.patient_service.respository.PatientRepository;
import com.hms.patient_service.util.PatientIdGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PatientIdGenerator patientIdGenerator;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper, PatientIdGenerator patientIdGenerator, BillingServiceGrpcClient billingServiceGrpcClient) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.patientIdGenerator = patientIdGenerator;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
    }

    public PatientDto savePatient(PatientDto patientDto) {
        if(patientRepository.existsByEmail(patientDto.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists. ");
        }

        if(patientRepository.existsByPhoneNumber(patientDto.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException("A patient with this phone number already exists. ");
        }

        Patient patient = patientMapper.dtoToEntity(patientDto);

        // Hydrate necessary patient fields
        patient.setRegistrationDate(LocalDate.now());
        patient.setPatientId(patientIdGenerator.generateUniquePatientId());

        Patient savedPatient = patientRepository.save(patient);
        // Create the billing account for the patient
        BillingResponse billingAccount = billingServiceGrpcClient.createBillingAccount(savedPatient.getPatientId(), savedPatient.getFirstName(), savedPatient.getLastName(), savedPatient.getEmail());
        return patientMapper.entityToDto(savedPatient);
    }

    public PatientDto getPatientById(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with Id: " + patientId));
        return patientMapper.entityToDto(patient);
    }

    public List<PatientDto> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(patientMapper::entityToDto).toList();
    }

    public PatientDto updatePatient(String patientId, PatientDto patientDto) {
        Patient currentPatient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with Id: " + patientDto.getPatientId()));

        patientMapper.updateEntityWithDto(patientDto, currentPatient);

        Patient savedPatient = patientRepository.save(currentPatient);
        return patientMapper.entityToDto(savedPatient);
    }

    public void deletePatient(String patientId) {
        long resultCount = patientRepository.deleteByPatientId(patientId);
        if (resultCount == 0) throw new ResourceNotFoundException("Patient not found with Id: " + patientId);
    }
}
