package com.hms.billing_service.repository;

import com.hms.billing_service.model.BillingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillingItemRepository extends JpaRepository<BillingItem, UUID> {
    List<BillingItem> findByBillingAccountId(UUID billingAccountId);
}
