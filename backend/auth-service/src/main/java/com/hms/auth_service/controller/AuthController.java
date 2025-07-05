package com.hms.auth_service.controller;

import com.hms.auth_service.dto.*;

import com.hms.auth_service.service.AuthService;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "API Auth Service")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    @Operation(summary = "User Registration", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RegistrationResponseDto> register(@Valid @RequestBody RegistrationRequestDto registrationRequestDto) {
        RegistrationResponseDto registeredUser = authService.register(registrationRequestDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login and generate token on user login.")
    public ResponseEntity<LoginInfoResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        String accessToken = loginResponseDto.getAccessToken();
        String refreshToken = loginResponseDto.getRefreshToken();

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false) // false for local development, true for production
                .path("/")
                .maxAge(15 * 60) // 15 minutes
                .sameSite("Strict")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // set true for prod
                .path("/api") // limit refresh token usage to refresh endpoint
                .maxAge(24 * 60 * 60) // 1 day
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        LoginInfoResponseDto infoResponseDto = LoginInfoResponseDto.builder()
                .email(loginResponseDto.getEmail())
                .role(loginResponseDto.getRole())
                .build();

        return new ResponseEntity<>(infoResponseDto, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token from cookies")
    public ResponseEntity<String> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken == null || refreshToken.isEmpty()) {
            return new ResponseEntity<>("Refresh token not found", HttpStatus.UNAUTHORIZED);
        }

        RefreshTokenDto refreshTokenDto = authService.refreshAccessToken(refreshToken);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", refreshTokenDto.getAccessToken())
                .httpOnly(true)
                .secure(false) // false for local development, true for production
                .path("/")
                .maxAge(15 * 60) // 15 minutes
                .sameSite("Strict")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshTokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(false) // set true for prod
                .path("/api") // limit refresh token usage to refresh endpoint
                .maxAge(24 * 60 * 60) // 1 day
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return new ResponseEntity<>("Refreshed Access Token", HttpStatus.OK);
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null; // Not found
    }


    @PostMapping("/logout")
    @Operation(summary = "Logout user and clear token from cookies", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken == null || refreshToken.isEmpty()) {
            return new ResponseEntity<>("Refresh token not found", HttpStatus.UNAUTHORIZED);
        }

        authService.logout(refreshToken);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false) // false for local development, true for production
                .path("/")
                .maxAge(0) // Clear the cookie
                .sameSite("Strict")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false) // false for local development, true for production
                .path("/api") // limit refresh token usage to refresh endpoint
                .maxAge(0) // Clear the cookie
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }

    @GetMapping("/get/currentUser")
    @Operation(summary = "Return user info based on token from cookies to reauthenticate on page reload")
    public ResponseEntity<?> getCurrentUser(@CookieValue(value = "accessToken", required = true) String token) {
        LoginInfoResponseDto infoResponseDto = authService.reauthenticate(token);
        return new ResponseEntity<>(infoResponseDto, HttpStatus.OK);
    }


    @GetMapping("/validate")
    @Operation(summary = "Validate Token", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token is missing", HttpStatus.UNAUTHORIZED);
        }

        return authService.validateToken(authHeader.substring(7))
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>("Expired Access Token", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/extractRole")
    @Operation(summary = "Extract Role from token")
    public ResponseEntity<String> extractRoleFromToken(@RequestHeader("Authorization") String authHeader) throws JwtException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token is missing", HttpStatus.UNAUTHORIZED);
        }

        String role = authService.extractRoleFromJwt(authHeader.substring(7));
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
