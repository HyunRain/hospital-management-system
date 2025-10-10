package com.hms.billing_service.controller;

import com.hms.billing_service.dto.BillingItemPdfResponseDto;
import com.hms.billing_service.service.CloudStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing-item/pdf")
@RequiredArgsConstructor
@Tag(name = "BillingItemPdfController", description = "Billing Item PDF Controller")
public class BillingItemPdfController {
    private final CloudStorageService cloudStorageService;

    @GetMapping("/get")
    public ResponseEntity<BillingItemPdfResponseDto> getBillingItemPdf(@RequestParam String fileName) {
        BillingItemPdfResponseDto pdfUrl = cloudStorageService.generatePresignedPdfUrl(fileName);
        return new ResponseEntity<>(pdfUrl, HttpStatus.OK);
    }
}
