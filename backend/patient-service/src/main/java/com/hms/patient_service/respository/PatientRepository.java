package com.hms.patient_service.respository;

import com.hms.patient_service.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID>, JpaSpecificationExecutor<Patient> {
    boolean existsByPatientId(String patientId);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Patient> findByPatientId(String patientId);
    long deleteByPatientId(String patientId);
    List<Patient> findAllByPatientIdIn(List<String> patientIds);

    @Query("SELECT p.id FROM Patient p ORDER BY p.id ASC")
    Page<UUID> findPagedPatientIds(Pageable pageable);

    @Query("""
    SELECT p FROM Patient p
    LEFT JOIN FETCH p.knownAllergies
    LEFT JOIN FETCH p.pastMedicalHistory
    LEFT JOIN FETCH p.chronicDiseases
    LEFT JOIN FETCH p.currentMedications
    LEFT JOIN FETCH p.immunizationStatus
    LEFT JOIN FETCH p.surgicalHistory
    WHERE p.id IN :ids ORDER BY p.id ASC
    """)
    List<Patient> findPatientsWithAllCollections(@Param("ids") List<UUID> ids);

}
