package com.hms.auth_service.service;

import com.hms.auth_service.dto.*;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    RefreshTokenDto refreshAccessToken(String refreshToken);
    LoginInfoResponseDto reauthenticate(String token);
    RegistrationResponseDto register(RegistrationRequestDto registrationRequestDto);
    void logout(String token);
    boolean validateToken(String token);
    String extractRoleFromJwt(String token);
}
