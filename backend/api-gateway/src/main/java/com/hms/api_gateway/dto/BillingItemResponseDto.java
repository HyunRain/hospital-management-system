package com.hms.api_gateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BillingItemResponseDto {
    @NotBlank
    private String billingId;
    @NotBlank
    private String billingAccountId;
    @NotBlank
    private String patientId;
    @NotBlank
    private String patientName;
    @NotBlank
    private String billingItemType;
    @NotNull
    private BigDecimal unitPrice;
    @NotNull
    private Integer quantity;
    @NotNull
    private BigDecimal totalPrice;
    @NotBlank
    private String status;
    @NotBlank
    private String startDate;
    @NotBlank
    private String dueDate;
}
