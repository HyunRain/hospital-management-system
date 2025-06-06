package com.hms.auth_service.controller;

import com.hms.auth_service.dto.LoginRequestDto;
import com.hms.auth_service.dto.LoginResponseDto;
import com.hms.auth_service.dto.RegistrationRequestDto;
import com.hms.auth_service.dto.RegistrationResponseDto;

import com.hms.auth_service.service.AuthService;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authService.authenticate(loginRequestDto);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
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
