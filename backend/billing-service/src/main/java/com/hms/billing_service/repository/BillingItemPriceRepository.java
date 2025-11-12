package com.hms.billing_service.repository;

import com.hms.billing_service.enums.BillingItemType;
import com.hms.billing_service.model.BillingItemPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillingItemPriceRepository extends JpaRepository<BillingItemPrice, Long> {
    Optional<BillingItemPrice> findByBillingItemType(String billingItemType);
    boolean existsByBillingItemType(BillingItemType billingItemType);
    long deleteByBillingItemType(BillingItemType billingItemType);
}
