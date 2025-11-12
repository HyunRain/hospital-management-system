package com.hms.staff_service.grpc;

import auth.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class AuthServiceGrpcClient {
    private final AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    public AuthServiceGrpcClient(@Value("${auth.service.address}") String serverAddress,
                                    @Value("${auth.service.grpc.port:9083}") int serverPort) {
        log.info("Connecting to Auth Service GRPC at {}:{}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        authServiceBlockingStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public UserExistsResponse userExists(String email) {
        UserExistsRequest request = UserExistsRequest.newBuilder()
                .setEmail(email)
                .build();

        UserExistsResponse response = authServiceBlockingStub.userExists(request);
        log.info("UserExistsResponse received from Auth Service via GRPC: {}", response);
        return response;
    }

    public CreateUserResponse createUser(String email, String password, String role) {
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .setRole(role)
                .build();

        CreateUserResponse response = authServiceBlockingStub.createUser(request);
        log.info("CreateUserResponse received from Auth Service via GRPC: {}", response);
        return response;
    }
}
