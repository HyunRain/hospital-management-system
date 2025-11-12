package com.hms.billing_service.dto;

import com.hms.billing_service.enums.BillingItemType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BillingItemPriceRequestDto {
    @NotNull
    private BillingItemType billingItemType;
    @NotNull
    private BigDecimal unitPrice;
}
