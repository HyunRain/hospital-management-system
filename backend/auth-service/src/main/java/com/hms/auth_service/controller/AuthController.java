package com.hms.auth_service.controller;

import com.hms.auth_service.dto.*;

import com.hms.auth_service.service.AuthService;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        LoginResponseDto loginResponseDto = authService.authenticate(loginRequestDto);
        String jwtToken = loginResponseDto.getToken();

        ResponseCookie cookie = ResponseCookie.from("token", jwtToken)
                .httpOnly(true)
                .secure(false) // false for local development, true for production
                .path("/")
                .maxAge(15 * 60) // 15 minutes
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        LoginInfoResponseDto infoResponseDto = LoginInfoResponseDto.builder()
                .email(loginResponseDto.getEmail())
                .role(loginResponseDto.getRole())
                .build();

        return new ResponseEntity<>(infoResponseDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user and clear token from cookies", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false) // false for local development, true for production
                .path("/")
                .maxAge(0) // Clear the cookie
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }

    @GetMapping("/get/currentUser")
    @Operation(summary = "Return user info based on token from cookies to reauthenticate on page reload")
    public ResponseEntity<LoginInfoResponseDto> getCurrentUser(@CookieValue(value = "token", required = false) String token) {
        if (token == null || token.isEmpty()) {
            // Return 401 Unauthorized or empty response if user not authenticated
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        LoginInfoResponseDto infoResponseDto = authService.reauthenticate(token);
        return new ResponseEntity<>(infoResponseDto, HttpStatus.OK);
    }


    @GetMapping("/validate")
    @Operation(summary = "Validate Token", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return authService.validateToken(authHeader.substring(7))
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/extractRole")
    @Operation(summary = "Extract Role from token")
    public ResponseEntity<String> extractRoleFromToken(@RequestHeader("Authorization") String authHeader) throws JwtException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String role = authService.extractRoleFromJwt(authHeader.substring(7));
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
