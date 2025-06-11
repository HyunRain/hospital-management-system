package com.hms.billing_service.service;

import com.hms.billing_service.dto.BillingItemPriceRequestDto;
import com.hms.billing_service.dto.BillingItemPriceResponseDto;
import com.hms.billing_service.enums.BillingItemType;
import com.hms.billing_service.exceptions.ResourceNotFoundException;
import com.hms.billing_service.mapper.BillingItemPricesMapper;
import com.hms.billing_service.model.BillingItemPrice;
import com.hms.billing_service.repository.BillingItemPriceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BillingItemPriceServiceImpl implements BillingItemPriceService {
    private final BillingItemPriceRepository billingItemPriceRepository;
    private final BillingItemPricesMapper billingItemPricesMapper;

    public BillingItemPriceServiceImpl(BillingItemPriceRepository billingItemPriceRepository, BillingItemPricesMapper billingItemPricesMapper) {
        this.billingItemPriceRepository = billingItemPriceRepository;
        this.billingItemPricesMapper = billingItemPricesMapper;
    }

    @Override
    public BillingItemPriceResponseDto createBillingItemPrice(BillingItemPriceRequestDto billingItemPriceRequestDto) {
        if (billingItemPriceRepository.existsByBillingItemType(billingItemPriceRequestDto.getBillingItemType())) {
            throw new ResourceNotFoundException("Billing item price for type " + billingItemPriceRequestDto.getBillingItemType() + " already exists.");
        }

        BillingItemPrice billingItemPriceToSave = billingItemPricesMapper.dtoToEntity(billingItemPriceRequestDto);
        billingItemPriceToSave.setCurrency("EUR");
        BillingItemPrice savedBillingItemPrice = billingItemPriceRepository.save(billingItemPriceToSave);
        return billingItemPricesMapper.entityToDto(savedBillingItemPrice);
    }

    @Override
    public BillingItemPriceResponseDto updateBillingItemPrice(BillingItemType billingItemType, BillingItemPriceRequestDto billingItemPriceRequestDto) {
        BillingItemPrice billingItemPrice = billingItemPriceRepository.findByBillingItemType(billingItemType.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Billing item price for type " + billingItemType + " not found."));

        BillingItemPrice updatedBillingItemPrice = billingItemPricesMapper.updateEntityFromDto(billingItemPrice, billingItemPriceRequestDto);
        return billingItemPricesMapper.entityToDto(billingItemPriceRepository.save(updatedBillingItemPrice));
    }

    @Override
    public BillingItemPriceResponseDto getBillingItemPrice(BillingItemType billingItemType) {
        BillingItemPrice billingItemPrice = billingItemPriceRepository.findByBillingItemType(billingItemType.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Billing item price for type " + billingItemType + " not found."));

        return billingItemPricesMapper.entityToDto(billingItemPrice);
    }

    @Override
    public List<BillingItemPriceResponseDto> getAllBillingItemPrices() {
        List<BillingItemPrice> allBillingItemPrices = billingItemPriceRepository.findAll();
        return allBillingItemPrices.stream().map(billingItemPricesMapper::entityToDto).toList();
    }

    @Override
    public void deleteBillingItemPrice(BillingItemType billingItemType) {
        long deleteCount = billingItemPriceRepository.deleteByBillingItemType(billingItemType);
        if(deleteCount == 0) throw new ResourceNotFoundException("Billing item price for type " + billingItemType + " not found.");
    }
}
