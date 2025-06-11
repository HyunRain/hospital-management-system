package com.hms.billing_service.grpc;

import billing.BillingServiceGrpc.BillingServiceImplBase;
import billing.GetBillingItemResponse;
import billing.GetBillingResponse;
import com.hms.billing_service.enums.BillingItemStatus;
import com.hms.billing_service.enums.BillingItemType;
import com.hms.billing_service.enums.Status;
import com.hms.billing_service.exceptions.ResourceNotFoundException;
import com.hms.billing_service.mapper.BillingItemMapper;
import com.hms.billing_service.model.BillingAccount;
import com.hms.billing_service.model.BillingItem;
import com.hms.billing_service.model.BillingItemPrice;
import com.hms.billing_service.repository.BillingItemPriceRepository;
import com.hms.billing_service.repository.BillingItemRepository;
import com.hms.billing_service.repository.BillingRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
    private final BillingRepository billingRepository;
    private final BillingItemRepository billingItemRepository;
    private final BillingItemPriceRepository billingItemPriceRepository;
    private final BillingItemMapper billingItemMapper;

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    public BillingGrpcService(BillingRepository billingRepository, BillingItemRepository billingItemRepository, BillingItemPriceRepository billingItemPriceRepository, BillingItemMapper billingItemMapper) {
        this.billingRepository = billingRepository;
        this.billingItemRepository = billingItemRepository;
        this.billingItemPriceRepository = billingItemPriceRepository;
        this.billingItemMapper = billingItemMapper;
    }
    // This class will implement the gRPC service for billing operations.
    // It will handle requests related to billing, such as creating invoices,
    // processing payments, and managing billing records.

    @Override
    public void createBillingAccount(billing.CreateBillingRequest createBillingRequest, StreamObserver<GetBillingResponse> responseStreamObserver) {
        log.info("createBillingAccount received: {}", createBillingRequest.toString());

        // Business logic to create a billing account
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal paidAmount = BigDecimal.ZERO;
        BigDecimal dueAmount = BigDecimal.ZERO;

        BillingAccount billingAccount = BillingAccount.builder()
                .patientId(createBillingRequest.getPatientId())
                .email(createBillingRequest.getEmail())
                .totalAmount(totalAmount)
                .paidAmount(paidAmount)
                .dueAmount(dueAmount)
                .status(Status.PENDING)
                .build();

        BillingAccount savedBillingAccount = billingRepository.save(billingAccount);

        GetBillingResponse response = GetBillingResponse.newBuilder()
                .setAccountId(String.valueOf(savedBillingAccount.getId()))
                .setStatus(String.valueOf(savedBillingAccount.getStatus()))
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getBillingAccount(billing.GetBillingAccountRequest getBillingAccountRequest, StreamObserver<GetBillingResponse> responseStreamObserver) {
        log.info("getBillingAccount received: {}", getBillingAccountRequest.toString());

        // Business logic to retrieve a billing account
        String patientId = getBillingAccountRequest.getPatientId();

        BillingAccount billingAccount = billingRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Billing account not found for patient ID: " + patientId));

        //Fetch all billing items and set them as a list of GetBillingItemResponse
        List<BillingItem> billingItems = billingItemRepository.findByBillingAccountId(billingAccount.getId());
        List<GetBillingItemResponse> responseList = billingItems.stream()
                .map(item -> GetBillingItemResponse.newBuilder()
                        .setAccountId(String.valueOf(billingAccount.getId()))
                        .setPatientId(patientId)
                        .setBillingItemId(String.valueOf(item.getId()))
                        .setBillingItemType(item.getBillingItemType().toString())
                        .setUnitPrice(item.getUnitPrice().toPlainString())
                        .setQuantity(item.getQuantity())
                        .setTotalPrice(item.getTotalPrice().toPlainString())
                        .setStatus(item.getStatus().toString())
                        .build())
                .toList();

        GetBillingResponse response = GetBillingResponse.newBuilder()
                .setAccountId(String.valueOf(billingAccount.getId()))
                .setPatientId(String.valueOf(billingAccount.getPatientId()))
                .setEmail(billingAccount.getEmail())
                .setTotalAmount(billingAccount.getTotalAmount().toPlainString())
                .setPaidAmount(billingAccount.getPaidAmount().toPlainString())
                .setDueAmount(billingAccount.getDueAmount().toPlainString())
                .setStatus(String.valueOf(billingAccount.getStatus()))
                .addAllBillingItems(responseList)
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void createBillingItem(billing.CreateBillingItemRequest createBillingItemRequest, StreamObserver<GetBillingItemResponse> responseStreamObserver) {
        log.info("createBillingItem received: {}", createBillingItemRequest.toString());

        String patientId = createBillingItemRequest.getPatientId();

        BillingAccount billingAccount = billingRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Billing account not found for patient ID: " + patientId));

        // Fetch the current billing item price based on the billing item type and set the unit price and initial total price
        BillingItemPrice billingItemPrice = billingItemPriceRepository.findByBillingItemType(createBillingItemRequest.getBillingItemType())
                .orElseThrow(() -> new ResourceNotFoundException("Billing item price not found for type: " + createBillingItemRequest.getBillingItemType()));

        BigDecimal unitPrice = billingItemPrice.getUnitPrice();
        // 1 is the starting quantity. 1 Day = 1 Quantity (Hospital Stay: quantity increments by 1 for each day via a scheduled job)
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(1));

        BillingItem billingItem = BillingItem.builder()
                .billingAccount(billingAccount)
                .billingItemType(BillingItemType.valueOf(createBillingItemRequest.getBillingItemType()))
                .unitPrice(unitPrice)
                .quantity(1)
                .totalPrice(totalPrice)
                .status(BillingItemStatus.ACTIVE)
                .build();

        BillingItem savedBillingItem = billingItemRepository.save(billingItem);

        GetBillingItemResponse response = GetBillingItemResponse.newBuilder()
                .setAccountId(String.valueOf(billingAccount.getId()))
                .setPatientId(patientId)
                .setBillingItemId(String.valueOf(savedBillingItem.getId()))
                .setBillingItemType(savedBillingItem.getBillingItemType().toString())
                .setUnitPrice(savedBillingItem.getUnitPrice().toPlainString())
                .setQuantity(savedBillingItem.getQuantity())
                .setTotalPrice(savedBillingItem.getTotalPrice().toPlainString())
                .setStatus(savedBillingItem.getStatus().toString())
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void updateBillingItem(billing.UpdateBillingItemRequest updateBillingItemRequest, StreamObserver<GetBillingItemResponse> responseStreamObserver) {
        log.info("updateBillingItem received: {}", updateBillingItemRequest.toString());

        String billingItemId = updateBillingItemRequest.getBillingItemId();

        BillingItem billingItem = billingItemRepository.findById(UUID.fromString(billingItemId))
                .orElseThrow(() -> new ResourceNotFoundException("Billing item not found for ID: " + billingItemId));

        BillingItem updatedBillingItem = billingItemMapper.updateBillingItem(billingItem, updateBillingItemRequest);

        // Update total price if the quantity has changed
        if(!Objects.equals(billingItem.getQuantity(), updatedBillingItem.getQuantity())) {
            BigDecimal totalPrice = updatedBillingItem.getUnitPrice().multiply(BigDecimal.valueOf(updatedBillingItem.getQuantity()));
            updatedBillingItem.setTotalPrice(totalPrice);
        }

        BillingItem savedBillingItem = billingItemRepository.save(updatedBillingItem);

        GetBillingItemResponse response = GetBillingItemResponse.newBuilder()
                .setAccountId(String.valueOf(savedBillingItem.getBillingAccount().getId()))
                .setPatientId(savedBillingItem.getBillingAccount().getPatientId())
                .setBillingItemId(String.valueOf(savedBillingItem.getId()))
                .setBillingItemType(savedBillingItem.getBillingItemType().toString())
                .setUnitPrice(savedBillingItem.getUnitPrice().toPlainString())
                .setQuantity(savedBillingItem.getQuantity())
                .setTotalPrice(savedBillingItem.getTotalPrice().toPlainString())
                .setStatus(savedBillingItem.getStatus().toString())
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }



}
