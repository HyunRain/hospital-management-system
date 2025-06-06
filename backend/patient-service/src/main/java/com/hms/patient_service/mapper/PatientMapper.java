package com.hms.patient_service.mapper;

import com.hms.patient_service.dto.PatientDto;
import com.hms.patient_service.model.Patient;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientDto entityToDto(Patient patient) {
        return PatientDto.builder()
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .gender(patient.getGender())
                .dateOfBirth(patient.getDateOfBirth())
                .bloodGroup(patient.getBloodGroup())
                .maritalStatus(patient.getMaritalStatus())
                .phoneNumber(patient.getPhoneNumber())
                .email(patient.getPatientId())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactNumber(patient.getEmergencyContactNumber())
                .relationshipToEmergencyContact(patient.getRelationshipToEmergencyContact())
                .addressLine1(patient.getAddressLine1())
                .addressLine2(patient.getAddressLine2())
                .city(patient.getCity())
                .state(patient.getState())
                .country(patient.getCountry())
                .postalCode(patient.getPostalCode())
                .status(patient.getStatus())
                .patientId(patient.getPatientId())
                .referredBy(patient.getReferredBy())
                .knownAllergies(patient.getKnownAllergies())
                .pastMedicalHistory(patient.getPastMedicalHistory())
                .chronicDiseases(patient.getChronicDiseases())
                .currentMedications(patient.getCurrentMedications())
                .immunizationStatus(patient.getImmunizationStatus())
                .surgicalHistory(patient.getSurgicalHistory())
                .insuranceProvider(patient.getInsuranceProvider())
                .insurancePolicyNumber(patient.getInsurancePolicyNumber())
                .insuranceExpiryDate(patient.getInsuranceExpiryDate())
                .build();
    }

    public Patient dtoToEntity(PatientDto patientDto) {
        return Patient.builder()
                .firstName(patientDto.getFirstName())
                .lastName(patientDto.getLastName())
                .gender(patientDto.getGender())
                .dateOfBirth(patientDto.getDateOfBirth())
                .bloodGroup(patientDto.getBloodGroup())
                .maritalStatus(patientDto.getMaritalStatus())
                .phoneNumber(patientDto.getPhoneNumber())
                .email(patientDto.getEmail())
                .emergencyContactName(patientDto.getEmergencyContactName())
                .emergencyContactNumber(patientDto.getEmergencyContactNumber())
                .relationshipToEmergencyContact(patientDto.getRelationshipToEmergencyContact())
                .addressLine1(patientDto.getAddressLine1())
                .addressLine2(patientDto.getAddressLine2())
                .city(patientDto.getCity())
                .state(patientDto.getState())
                .country(patientDto.getCountry())
                .postalCode(patientDto.getPostalCode())
                .status(patientDto.getStatus())
                .referredBy(patientDto.getReferredBy())
                .knownAllergies(patientDto.getKnownAllergies())
                .pastMedicalHistory(patientDto.getPastMedicalHistory())
                .chronicDiseases(patientDto.getChronicDiseases())
                .currentMedications(patientDto.getCurrentMedications())
                .immunizationStatus(patientDto.getImmunizationStatus())
                .surgicalHistory(patientDto.getSurgicalHistory())
                .insuranceProvider(patientDto.getInsuranceProvider())
                .insurancePolicyNumber(patientDto.getInsurancePolicyNumber())
                .insuranceExpiryDate(patientDto.getInsuranceExpiryDate())
                .build();
    }

    public void updateEntityWithDto(PatientDto dto, Patient patient) {
        if (!Objects.equals(patient.getFirstName(), dto.getFirstName())) {
            patient.setFirstName(dto.getFirstName());
        }
        if (!Objects.equals(patient.getLastName(), dto.getLastName())) {
            patient.setLastName(dto.getLastName());
        }
        if (!Objects.equals(patient.getGender(), dto.getGender())) {
            patient.setGender(dto.getGender());
        }
        if (!Objects.equals(patient.getDateOfBirth(), dto.getDateOfBirth())) {
            patient.setDateOfBirth(dto.getDateOfBirth());
        }
        if (!Objects.equals(patient.getBloodGroup(), dto.getBloodGroup())) {
            patient.setBloodGroup(dto.getBloodGroup());
        }
        if (!Objects.equals(patient.getMaritalStatus(), dto.getMaritalStatus())) {
            patient.setMaritalStatus(dto.getMaritalStatus());
        }
        if (!Objects.equals(patient.getPhoneNumber(), dto.getPhoneNumber())) {
            patient.setPhoneNumber(dto.getPhoneNumber());
        }
        if (!Objects.equals(patient.getEmail(), dto.getEmail())) {
            patient.setEmail(dto.getEmail());
        }
        if (!Objects.equals(patient.getEmergencyContactName(), dto.getEmergencyContactName())) {
            patient.setEmergencyContactName(dto.getEmergencyContactName());
        }
        if (!Objects.equals(patient.getEmergencyContactNumber(), dto.getEmergencyContactNumber())) {
            patient.setEmergencyContactNumber(dto.getEmergencyContactNumber());
        }
        if (!Objects.equals(patient.getRelationshipToEmergencyContact(), dto.getRelationshipToEmergencyContact())) {
            patient.setRelationshipToEmergencyContact(dto.getRelationshipToEmergencyContact());
        }
        if (!Objects.equals(patient.getAddressLine1(), dto.getAddressLine1())) {
            patient.setAddressLine1(dto.getAddressLine1());
        }
        if (!Objects.equals(patient.getAddressLine2(), dto.getAddressLine2())) {
            patient.setAddressLine2(dto.getAddressLine2());
        }
        if (!Objects.equals(patient.getCity(), dto.getCity())) {
            patient.setCity(dto.getCity());
        }
        if (!Objects.equals(patient.getState(), dto.getState())) {
            patient.setState(dto.getState());
        }
        if (!Objects.equals(patient.getCountry(), dto.getCountry())) {
            patient.setCountry(dto.getCountry());
        }
        if (!Objects.equals(patient.getPostalCode(), dto.getPostalCode())) {
            patient.setPostalCode(dto.getPostalCode());
        }
        if (!Objects.equals(patient.getStatus(), dto.getStatus())) {
            patient.setStatus(dto.getStatus());
        }
        if (!Objects.equals(patient.getReferredBy(), dto.getReferredBy())) {
            patient.setReferredBy(dto.getReferredBy());
        }
        if (!Objects.equals(patient.getKnownAllergies(), dto.getKnownAllergies())) {
            patient.setKnownAllergies(dto.getKnownAllergies());
        }
        if (!Objects.equals(patient.getPastMedicalHistory(), dto.getPastMedicalHistory())) {
            patient.setPastMedicalHistory(dto.getPastMedicalHistory());
        }
        if (!Objects.equals(patient.getChronicDiseases(), dto.getChronicDiseases())) {
            patient.setChronicDiseases(dto.getChronicDiseases());
        }
        if (!Objects.equals(patient.getCurrentMedications(), dto.getCurrentMedications())) {
            patient.setCurrentMedications(dto.getCurrentMedications());
        }
        if (!Objects.equals(patient.getImmunizationStatus(), dto.getImmunizationStatus())) {
            patient.setImmunizationStatus(dto.getImmunizationStatus());
        }
        if (!Objects.equals(patient.getSurgicalHistory(), dto.getSurgicalHistory())) {
            patient.setSurgicalHistory(dto.getSurgicalHistory());
        }
        if (!Objects.equals(patient.getInsuranceProvider(), dto.getInsuranceProvider())) {
            patient.setInsuranceProvider(dto.getInsuranceProvider());
        }
        if (!Objects.equals(patient.getInsurancePolicyNumber(), dto.getInsurancePolicyNumber())) {
            patient.setInsurancePolicyNumber(dto.getInsurancePolicyNumber());
        }
        if (!Objects.equals(patient.getInsuranceExpiryDate(), dto.getInsuranceExpiryDate())) {
            patient.setInsuranceExpiryDate(dto.getInsuranceExpiryDate());
        }
    }

}
