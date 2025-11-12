package com.hms.billing_service.service;

import com.hms.billing_service.dto.BillingItemPriceRequestDto;
import com.hms.billing_service.dto.BillingItemPriceResponseDto;
import com.hms.billing_service.enums.BillingItemType;

import java.util.List;

public interface BillingItemPriceService {
    BillingItemPriceResponseDto createBillingItemPrice(BillingItemPriceRequestDto billingItemPriceRequestDto);
    BillingItemPriceResponseDto updateBillingItemPrice(BillingItemType billingItemType, BillingItemPriceRequestDto billingItemPriceRequestDto);
    BillingItemPriceResponseDto getBillingItemPrice(BillingItemType billingItemType);
    List<BillingItemPriceResponseDto> getAllBillingItemPrices();
    void deleteBillingItemPrice(BillingItemType billingItemType);
}
