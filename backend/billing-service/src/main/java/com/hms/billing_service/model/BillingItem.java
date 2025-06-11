package com.hms.billing_service.model;


import com.hms.billing_service.enums.BillingItemStatus;
import com.hms.billing_service.enums.BillingItemType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BillingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_account_id", nullable = false)
    private BillingAccount billingAccount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BillingItemType billingItemType;

    @NotNull
    private BigDecimal unitPrice;
    @Min(1)
    private int quantity;
    @NotNull
    private BigDecimal totalPrice;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BillingItemStatus status;

    @CreatedDate
    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
