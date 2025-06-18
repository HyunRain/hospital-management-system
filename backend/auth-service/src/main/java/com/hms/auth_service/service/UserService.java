package com.hms.auth_service.service;

import com.hms.auth_service.dto.RegistrationRequestDto;
import com.hms.auth_service.model.User;

import java.util.Optional;

public interface UserService {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    User registerUser(RegistrationRequestDto registrationRequestDto, String encodedPassword);
}
