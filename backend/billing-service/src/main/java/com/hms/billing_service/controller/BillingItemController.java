package com.hms.billing_service.controller;

import com.hms.billing_service.dto.PaginatedBillingItemResponseDto;
import com.hms.billing_service.service.BillingItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/billing")
@Tag(name = "BillingItemController", description = "Billing Item Controller")
public class BillingItemController {
    private final BillingItemService billingItemService;

    public BillingItemController(BillingItemService billingItemService) {
        this.billingItemService = billingItemService;
    }

    @GetMapping("/all")
    public ResponseEntity<PaginatedBillingItemResponseDto> getPaginatedBillingItems(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size) {
        PaginatedBillingItemResponseDto paginatedBillingItems = billingItemService.getPaginatedBillingItems(page, size);
        return new ResponseEntity<>(paginatedBillingItems, HttpStatus.OK);
    }
}
