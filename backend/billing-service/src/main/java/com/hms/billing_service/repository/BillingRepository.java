package com.hms.billing_service.repository;

import com.hms.billing_service.model.BillingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillingRepository extends JpaRepository<BillingAccount, UUID> {
    Optional<BillingAccount> findByEmail(String email);
    Optional<BillingAccount> findByPatientId(String patientId);
}
