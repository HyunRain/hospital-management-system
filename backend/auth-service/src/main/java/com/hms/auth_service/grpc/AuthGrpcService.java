package com.hms.auth_service.grpc;

import auth.AuthServiceGrpc.AuthServiceImplBase;
import auth.CreateUserResponse;
import auth.UserExistsResponse;
import com.hms.auth_service.enums.Role;
import com.hms.auth_service.model.User;
import com.hms.auth_service.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

@GrpcService
@Transactional
public class AuthGrpcService extends AuthServiceImplBase {
    private final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(AuthGrpcService.class);

    public AuthGrpcService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void userExists(auth.UserExistsRequest userExistsRequest, StreamObserver<UserExistsResponse> responseStreamObserver) {
        log.info("userExistsRequest received: {}", userExistsRequest.toString());

        boolean doesExist = userRepository.existsByEmail(userExistsRequest.getEmail());

        UserExistsResponse response = UserExistsResponse.newBuilder()
                .setDoesExist(doesExist)
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void createUser(auth.CreateUserRequest createUserRequest, StreamObserver<CreateUserResponse> responseStreamObserver) {
        log.info("createUserRequest received: {}", createUserRequest.toString());

        String encodedPassword = passwordEncoder.encode(createUserRequest.getPassword());
        User userToSave = User.builder()
                .email(createUserRequest.getEmail())
                .password(encodedPassword)
                .role(Role.valueOf(createUserRequest.getRole()))
                .build();

        User savedUser = userRepository.save(userToSave);

        CreateUserResponse response = CreateUserResponse.newBuilder()
                .setUserId(String.valueOf(savedUser.getId()))
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }


}
