package com.hms.billing_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedBillingItemResponseDto {
    private List<BillingItemResponseDto> billingItemResponseDtos;
    private int totalPages;
    private long totalBillingItems;
}
