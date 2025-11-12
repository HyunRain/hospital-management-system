package com.hms.billing_service.service;

import com.hms.billing_service.dto.BillingItemPdfResponseDto;

public interface CloudStorageService {
    BillingItemPdfResponseDto generatePresignedPdfUrl(String fileName);
}
