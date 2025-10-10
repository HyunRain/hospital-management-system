package com.hms.billing_service.service;

import com.hms.billing_service.dto.BillingItemResponseDto;
import com.hms.billing_service.dto.PaginatedBillingItemResponseDto;
import com.hms.billing_service.mapper.BillingItemMapper;
import com.hms.billing_service.model.BillingItem;
import com.hms.billing_service.repository.BillingItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class BillingItemServiceImpl implements BillingItemService {
    private final BillingItemRepository billingItemRepository;
    private final BillingItemMapper billingItemMapper;

    public BillingItemServiceImpl(BillingItemRepository billingItemRepository, BillingItemMapper billingItemMapper) {
        this.billingItemRepository = billingItemRepository;
        this.billingItemMapper = billingItemMapper;
    }

    public PaginatedBillingItemResponseDto getPaginatedBillingItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<BillingItem> pagedBillingItems = billingItemRepository.findPagedBillingItems(pageable);
        List<BillingItem> billingItems = pagedBillingItems.stream().toList();
        List<BillingItemResponseDto> dtos = billingItems.stream().map(billingItemMapper::entityToDto).toList();

        return PaginatedBillingItemResponseDto.builder()
                .billingItemResponseDtos(dtos)
                .totalPages(pagedBillingItems.getTotalPages())
                .totalBillingItems(pagedBillingItems.getTotalElements())
                .build();
    }
}
