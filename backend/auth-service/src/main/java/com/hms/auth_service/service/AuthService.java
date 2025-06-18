package com.hms.auth_service.service;

import com.hms.auth_service.dto.*;

public interface AuthService {
    LoginResponseDto authenticate(LoginRequestDto loginRequestDto);
    LoginInfoResponseDto reauthenticate(String token);
    RegistrationResponseDto register(RegistrationRequestDto registrationRequestDto);
    boolean validateToken(String token);
    String extractRoleFromJwt(String token);
}
