package com.hms.billing_service.repository;

import com.hms.billing_service.model.BillingItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillingItemRepository extends JpaRepository<BillingItem, UUID> {
    List<BillingItem> findByBillingAccountId(UUID billingAccountId);
    Optional<BillingItem> findByIdAndBillingAccountId(UUID id, UUID billingAccountId);

    @Query( value = "SELECT b FROM BillingItem b JOIN FETCH b.billingAccount ORDER BY b.startDate DESC",
            countQuery = "SELECT COUNT(b) FROM BillingItem b")
    Page<BillingItem> findPagedBillingItems(Pageable pageable);
}
