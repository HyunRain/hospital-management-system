package com.hms.auth_service.service;

import com.hms.auth_service.dto.*;
import com.hms.auth_service.exception.EmailAlreadyExistsException;
import com.hms.auth_service.model.User;
import com.hms.auth_service.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;


@Transactional
@Service
public class AuthServiceImpl implements AuthService {
    public final UserService userService;
    public final PasswordEncoder passwordEncoder;
    public final JwtUtil jwtUtil;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public RegistrationResponseDto register(RegistrationRequestDto registrationRequestDto) {
        if (userService.existsByEmail(registrationRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("This email is already registered");
        }

        if (!Objects.equals(registrationRequestDto.getPassword(), registrationRequestDto.getRepeatedPassword())) {
            throw new BadCredentialsException("Passwords do not match");
        }

        String encodedPassword = passwordEncoder.encode(registrationRequestDto.getPassword());
        User savedUser = userService.registerUser(registrationRequestDto, encodedPassword);
        return RegistrationResponseDto.builder()
                .email(savedUser.getEmail())
                .build();
    }

    public LoginResponseDto authenticate(LoginRequestDto loginRequestDto) {
        User user = userService.findByEmail(loginRequestDto.getEmail());

        System.out.println(user.getEmail());
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        return LoginResponseDto.builder()
                .token(jwtUtil.generateToken(user.getEmail(), user.getRole()))
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public LoginInfoResponseDto reauthenticate(String token) {
        return jwtUtil.extractEmailAndRoleFromJwt(token);
    }

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractRoleFromJwt(String token) {
        return jwtUtil.extractRoleFromJwt(token);
    }

}
