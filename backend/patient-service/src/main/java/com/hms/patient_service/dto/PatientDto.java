package com.hms.patient_service.dto;
import com.hms.patient_service.enums.BloodType;
import com.hms.patient_service.enums.Gender;
import com.hms.patient_service.enums.MaritalStatus;
import com.hms.patient_service.enums.PatientStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class PatientDto {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must be less than 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must be less than 100 characters")
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Blood group is required")
    private BloodType bloodGroup;

    @NotNull(message = "Marital Status is required")
    private MaritalStatus maritalStatus;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Email(message = "Email needs to be valid")
    @NotBlank(message = "Email is required")
    private String email;

    private String emergencyContactName;
    private String emergencyContactNumber;
    private String relationshipToEmergencyContact;

    @NotBlank(message = "Address is required")
    private String addressLine1;

    private String addressLine2;
    @NotBlank(message = "City is required")
    private String city;
    @NotBlank(message = "State is required")
    private String state;
    @NotBlank(message = "Country is required")
    private String country;
    @NotBlank(message = "Postal code is required")
    private String postalCode;

    @NotNull(message = "Status is required")
    private PatientStatus status;
    private String patientId;

    private String referredBy;

    private Set<String> knownAllergies;
    private Set<String> pastMedicalHistory;
    private Set<String> chronicDiseases;
    private Set<String> currentMedications;
    private Set<String> immunizationStatus;
    private Set<String> surgicalHistory;

    private String insuranceProvider;
    private String insurancePolicyNumber;
    private LocalDate insuranceExpiryDate;
}