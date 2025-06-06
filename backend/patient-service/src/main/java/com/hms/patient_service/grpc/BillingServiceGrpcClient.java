package com.hms.patient_service.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
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

    public BillingResponse createBillingAccount(String patientId, String firstName, String lastName, String email) {
        BillingRequest request = BillingRequest.newBuilder().setPatientId(patientId).setFirstName(firstName).setLastName(lastName).setEmail(email).build();

        BillingResponse response = billingServiceBlockingStub.createBillingAccount(request);
        log.info("Response received from Billing Service via GRPC: {}", response);
        return response;
    }


}
