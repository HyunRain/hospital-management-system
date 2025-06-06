package com.hms.auth_service.service;

import com.hms.auth_service.dto.LoginRequestDto;
import com.hms.auth_service.dto.LoginResponseDto;
import com.hms.auth_service.dto.RegistrationRequestDto;
import com.hms.auth_service.dto.RegistrationResponseDto;

public interface AuthService {
    LoginResponseDto authenticate(LoginRequestDto loginRequestDto);
    RegistrationResponseDto register(RegistrationRequestDto registrationRequestDto);
    boolean validateToken(String token);
    String extractRoleFromJwt(String token);
}
