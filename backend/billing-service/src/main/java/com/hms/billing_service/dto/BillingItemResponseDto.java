package com.hms.billing_service.dto;

import com.hms.billing_service.enums.BillingItemStatus;
import com.hms.billing_service.enums.BillingItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BillingItemResponseDto {
    @NotBlank
    private UUID billingId;
    @NotBlank
    private UUID billingAccountId;
    @NotBlank
    private String patientId;
    @NotBlank
    private BillingItemType billingItemType;
    @NotNull
    private BigDecimal unitPrice;
    @NotNull
    private Integer quantity;
    @NotNull
    private BigDecimal totalPrice;
    @NotBlank
    private BillingItemStatus status;
    @NotBlank
    private LocalDateTime startDate;
    @NotBlank
    private LocalDateTime dueDate;
}
