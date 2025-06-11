package com.hms.billing_service.mapper;

import com.hms.billing_service.enums.BillingItemStatus;
import com.hms.billing_service.enums.BillingItemType;
import com.hms.billing_service.model.BillingItem;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BillingItemMapper {
    public BillingItem updateBillingItem(BillingItem existingItem, billing.UpdateBillingItemRequest updateBillingItemRequest) {
        if (!Objects.equals(existingItem.getBillingItemType().toString(), updateBillingItemRequest.getBillingItemType())) {
            existingItem.setBillingItemType(BillingItemType.valueOf(updateBillingItemRequest.getBillingItemType()));
        }

        if (!Objects.equals(existingItem.getQuantity(), updateBillingItemRequest.getQuantity())) {
            existingItem.setQuantity(updateBillingItemRequest.getQuantity());
        }

        if (!Objects.equals(existingItem.getStatus(), BillingItemStatus.valueOf(updateBillingItemRequest.getStatus()))) {
            existingItem.setStatus(BillingItemStatus.valueOf(updateBillingItemRequest.getStatus()));
        }

        return existingItem;
    }
}
