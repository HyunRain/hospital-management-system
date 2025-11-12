package com.hms.appointment_service.grpc;


import billing.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingServiceGrpcClient {
    private final BillingServiceGrpc.BillingServiceBlockingStub billingServiceBlockingStub;

    public BillingServiceGrpcClient(@Value("${billing.service.address}") String serverAddress,
                                    @Value("${billing.service.grpc.port:9082}") int serverPort) {
        log.info("Connecting to Billing Service GRPC at {}:{}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        billingServiceBlockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public GetBillingItemResponse createBillingItem(String patientId, String billingItemType, String sourceId) {
        CreateBillingItemRequest request = CreateBillingItemRequest.newBuilder()
                .setPatientId(patientId)
                .setBillingItemType(billingItemType)
                .setSourceId(sourceId)
                .build();

        GetBillingItemResponse response = billingServiceBlockingStub.createBillingItem(request);
        log.info("Create BillingItem Response received from Billing Service via GRPC: {}", response);
        return response;
    }

    public GetBillingItemResponse updateBillingItem(String billingItemId, String accountId, String billingItemType, String startDate, String sourceId, String status) {
        UpdateBillingItemRequest request = UpdateBillingItemRequest.newBuilder()
                .setAccountId(accountId)
                .setBillingItemId(billingItemId)
                .setBillingItemType(billingItemType)
                .setSourceId(sourceId)
                .setStartDate(startDate)
                .setStatus(status)
                .build();

        GetBillingItemResponse response = billingServiceBlockingStub.updateBillingItem(request);
        log.info("Update BillingItem Response received from Billing Service via GRPC: {}", response);
        return response;
    }



}
