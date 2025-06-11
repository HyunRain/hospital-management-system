package com.hms.billing_service.mapper;

import com.hms.billing_service.dto.BillingItemPriceRequestDto;
import com.hms.billing_service.dto.BillingItemPriceResponseDto;
import com.hms.billing_service.model.BillingItemPrice;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BillingItemPricesMapper {
    public BillingItemPriceResponseDto entityToDto(BillingItemPrice billingItemPrice ) {
        return BillingItemPriceResponseDto.builder()
                .id(billingItemPrice.getId())
                .billingItemType(billingItemPrice.getBillingItemType())
                .unitPrice(billingItemPrice.getUnitPrice())
                .currency(billingItemPrice.getCurrency())
                .build();
    }

    public BillingItemPrice dtoToEntity(BillingItemPriceRequestDto billingItemPriceRequestDto) {
        return BillingItemPrice.builder()
                .billingItemType(billingItemPriceRequestDto.getBillingItemType())
                .unitPrice(billingItemPriceRequestDto.getUnitPrice())
                .build();
    }

    public BillingItemPrice updateEntityFromDto(BillingItemPrice billingItemPrice, BillingItemPriceRequestDto billingItemPriceRequestDto) {
        if (!Objects.equals(billingItemPrice.getUnitPrice(), billingItemPriceRequestDto.getUnitPrice())) {
            billingItemPrice.setUnitPrice(billingItemPriceRequestDto.getUnitPrice());
        }
        return billingItemPrice;
    }
}
