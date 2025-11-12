package com.hms.billing_service.model;

import com.hms.billing_service.enums.BillingItemType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BillingItemPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull()
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private BillingItemType billingItemType;

    @NotNull
    private BigDecimal unitPrice;

    @NotBlank
    private String currency;
}
