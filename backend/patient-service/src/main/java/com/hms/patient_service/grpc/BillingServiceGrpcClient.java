package com.hms.patient_service.grpc;


import billing.BillingServiceGrpc;
import billing.CreateBillingRequest;
import billing.GetBillingAccountRequest;
import billing.GetBillingResponse;
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

    public GetBillingResponse createBillingAccount(String patientId, String firstName, String lastName, String email) {
        CreateBillingRequest request = CreateBillingRequest.newBuilder().setPatientId(patientId).setEmail(email).build();

        GetBillingResponse response = billingServiceBlockingStub.createBillingAccount(request);
        log.info("Create BillingAccount Response received from Billing Service via GRPC: {}", response);
        return response;
    }

    public GetBillingResponse getBillingAccount(String patientId) {
        GetBillingAccountRequest request = GetBillingAccountRequest.newBuilder().setPatientId(patientId).build();

        GetBillingResponse response = billingServiceBlockingStub.getBillingAccount(request);
        log.info("Get BillingAccount Response received from Billing Service via GRPC: {}", response);
        return response;
    }


}
