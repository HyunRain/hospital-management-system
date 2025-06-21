package com.hms.patient_service.model;
import com.hms.patient_service.enums.BloodType;
import com.hms.patient_service.enums.Gender;
import com.hms.patient_service.enums.MaritalStatus;
import com.hms.patient_service.enums.PatientStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class  Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BloodType bloodGroup;
    @NotNull
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @NotNull
    @Column(unique = true)
    private String phoneNumber;
    @NotNull
    @Email
    @Column(unique = true)
    private String email;
    @NotNull
    private String emergencyContactName;
    @NotNull
    private String emergencyContactNumber;
    @NotNull
    private String relationshipToEmergencyContact;

    @NotNull
    @Column(name = "address")
    private String addressLine1;
    @Column(name = "2nd_address", nullable = true)
    private String addressLine2;
    @NotNull
    private String city;
    @NotNull
    private String state;
    @NotNull
    private String country;
    @NotNull
    private String postalCode;

    @NotNull
    @Column(unique = true)
    private String patientId;
    @NotNull
    private LocalDate registrationDate;
    @NotNull
    @Enumerated(EnumType.STRING)
    private PatientStatus status; // ACTIVE, INACTIVE, DECEASED, etc.
    @NotNull
    private String referredBy;

    @NotNull
    @ElementCollection
    @CollectionTable(name = "patient_known_allergies", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "allergy")
    private List<String> knownAllergies;
    @NotNull
    @ElementCollection
    @CollectionTable(name = "patient_past_medical_history", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "medicalHistory")
    private List<String> pastMedicalHistory;
    @NotNull
    @ElementCollection
    @CollectionTable(name = "patient_chronic_diseases", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "disease")
    private List<String> chronicDiseases;
    @NotNull
    @ElementCollection
    @CollectionTable(name = "patient_current_medications", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "medication")
    private List<String> currentMedications;
    @NotNull
    @ElementCollection
    @CollectionTable(name = "patient_immunization_status", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "immunization")
    private List<String> immunizationStatus;
    @NotNull
    @ElementCollection
    @CollectionTable(name = "patient_surgical_history", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "surgery")
    private List<String> surgicalHistory;

    @NotNull
    private String insuranceProvider;
    @NotNull
    private String insurancePolicyNumber;
    @NotNull
    private LocalDate insuranceExpiryDate;


    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

//    @CreatedBy
//    private String createdBy;
//
//    @LastModifiedBy
//    private String updatedBy;
}
