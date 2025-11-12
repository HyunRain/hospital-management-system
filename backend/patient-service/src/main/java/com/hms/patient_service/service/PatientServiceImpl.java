package com.hms.patient_service.service;


import billing.GetBillingResponse;
import com.hms.patient_service.dto.PaginatedResponse;
import com.hms.patient_service.dto.SimplePatientDto;
import com.hms.patient_service.exception.EmailAlreadyExistsException;
import com.hms.patient_service.exception.PhoneNumberAlreadyExistsException;
import com.hms.patient_service.exception.ResourceNotFoundException;
import com.hms.patient_service.dto.PatientDto;
import com.hms.patient_service.grpc.BillingServiceGrpcClient;
import com.hms.patient_service.mapper.PatientMapper;
import com.hms.patient_service.model.Patient;
import com.hms.patient_service.respository.PatientRepository;
import com.hms.patient_service.specification.PatientSpecifications;
import com.hms.patient_service.util.PatientIdGenerator;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @Override
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
        GetBillingResponse billingAccount = billingServiceGrpcClient.createBillingAccount(savedPatient.getPatientId(), savedPatient.getFirstName(), savedPatient.getLastName(), savedPatient.getEmail());
        return patientMapper.entityToDto(savedPatient);
    }

    @Override
    public PatientDto getPatientById(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with Id: " + patientId));
        return patientMapper.entityToDto(patient);
    }

    @Override
    public PaginatedResponse getAllPatientsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UUID> pagedPatientIds = patientRepository.findPagedPatientIds(pageable);
        List<UUID> patientIds = pagedPatientIds.stream().toList();
        List<Patient> allPatients = patientRepository.findPatientsWithAllCollections(patientIds);
        List<PatientDto> dtos = allPatients.stream().map(patientMapper::entityToDto).toList();

        System.out.println(LocalDateTime.now());
        return PaginatedResponse.builder()
                .patients(dtos)
                .totalPages(pagedPatientIds.getTotalPages())
                .totalPatients(pagedPatientIds.getTotalElements())
                .build();
    }

    @Override
    public List<SimplePatientDto> getPatientsByIds(List<String> patientIds) {
        List<Patient> allPatients = patientRepository.findAllByPatientIdIn(patientIds);
        return allPatients.stream().map(patientMapper::entityToSimpleDto).toList();
    }

    @Override
    public PaginatedResponse searchPatients(String input, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Patient> spec = PatientSpecifications.patientContainsTerm(input);

        Page<Patient> pagedPatients = patientRepository.findAll(spec, pageable);
        List<UUID> patientIds = pagedPatients.stream().map(Patient::getId).toList();

        List<Patient> allPatients = patientRepository.findPatientsWithAllCollections(patientIds);

        List<PatientDto> dtos = allPatients.stream().map(patientMapper::entityToDto).toList();

        return PaginatedResponse.builder()
                .patients(dtos)
                .totalPages(pagedPatients.getTotalPages())
                .totalPatients(pagedPatients.getTotalElements())
                .build();
    }


        public PatientDto updatePatient(String patientId, PatientDto patientDto) {
        Patient currentPatient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with Id: " + patientDto.getPatientId()));

        patientMapper.updateEntityWithDto(patientDto, currentPatient);

        Patient savedPatient = patientRepository.save(currentPatient);
        return patientMapper.entityToDto(savedPatient);
    }

    @Override
    public void deletePatient(String patientId) {
        long resultCount = patientRepository.deleteByPatientId(patientId);
        if (resultCount == 0) throw new ResourceNotFoundException("Patient not found with Id: " + patientId);
    }
}
