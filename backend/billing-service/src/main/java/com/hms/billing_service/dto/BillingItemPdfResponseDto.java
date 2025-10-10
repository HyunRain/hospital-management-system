package com.hms.billing_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillingItemPdfResponseDto {
    private String pdfUrl;
}
