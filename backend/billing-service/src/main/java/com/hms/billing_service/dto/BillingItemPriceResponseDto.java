package com.hms.billing_service.dto;

import com.hms.billing_service.enums.BillingItemType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BillingItemPriceResponseDto {
    private Long id;
    private BillingItemType billingItemType;
    private BigDecimal unitPrice;
    private String currency;
}
