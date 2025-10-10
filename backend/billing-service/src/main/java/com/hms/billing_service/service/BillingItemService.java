package com.hms.billing_service.service;

import com.hms.billing_service.dto.PaginatedBillingItemResponseDto;

public interface BillingItemService {
    PaginatedBillingItemResponseDto getPaginatedBillingItems(int page, int size);
}
