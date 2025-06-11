package com.hms.billing_service.controller;


import com.hms.billing_service.dto.BillingItemPriceRequestDto;
import com.hms.billing_service.dto.BillingItemPriceResponseDto;
import com.hms.billing_service.enums.BillingItemType;
import com.hms.billing_service.service.BillingItemPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing-item-prices")
@Tag(name = "Billing Item Prices", description = "Controller for managing billing item prices")
public class BillingItemPricesController {
    private final BillingItemPriceService billingItemPriceService;

    public BillingItemPricesController(BillingItemPriceService billingItemPriceService) {
        this.billingItemPriceService = billingItemPriceService;
    }

    @PostMapping
    @Operation(summary = "Create a new billing item price")
    public ResponseEntity<BillingItemPriceResponseDto> createBillingItemPrice(@Valid @RequestBody BillingItemPriceRequestDto billingItemPriceRequestDto) {
        BillingItemPriceResponseDto billingItemPrice = billingItemPriceService.createBillingItemPrice(billingItemPriceRequestDto);
        return new ResponseEntity<>(billingItemPrice, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{type}")
    @Operation(summary = "Update billing item price by type")
    public ResponseEntity<BillingItemPriceResponseDto> updateBillingItemPrice(@PathVariable BillingItemType type, @Valid @RequestBody BillingItemPriceRequestDto billingItemPriceRequestDto) {
        BillingItemPriceResponseDto updatedBillingItemPrice = billingItemPriceService.updateBillingItemPrice(type, billingItemPriceRequestDto);
        return new ResponseEntity<>(updatedBillingItemPrice, HttpStatus.OK);
    }

    @GetMapping("/get/{type}")
    @Operation(summary = "Get billing item price by type")
    public ResponseEntity<BillingItemPriceResponseDto> getBillingItemPrice(@PathVariable BillingItemType type) {
        BillingItemPriceResponseDto billingItemPrice = billingItemPriceService.getBillingItemPrice(type);
        return new ResponseEntity<>(billingItemPrice, HttpStatus.OK);
    }

    @GetMapping("/get/all")
    @Operation(summary = "Get all billing item prices")
    public ResponseEntity<List<BillingItemPriceResponseDto>> getAllBillingItemPrices() {
        List<BillingItemPriceResponseDto> allBillingItemPrices = billingItemPriceService.getAllBillingItemPrices();
        return new ResponseEntity<>(allBillingItemPrices, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{type}")
    @Operation(summary = "Delete billing item price by type")
    public ResponseEntity<Void> deleteBillingItemPrice(@PathVariable BillingItemType type) {
        billingItemPriceService.deleteBillingItemPrice(type);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
