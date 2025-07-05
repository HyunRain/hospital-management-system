package com.hms.auth_service.service;

import com.hms.auth_service.dto.*;
import com.hms.auth_service.enums.Role;
import com.hms.auth_service.exception.EmailAlreadyExistsException;
import com.hms.auth_service.exception.ResourceNotFoundException;
import com.hms.auth_service.model.RefreshToken;
import com.hms.auth_service.model.User;
import com.hms.auth_service.repository.RefreshTokenRepository;
import com.hms.auth_service.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;


@Transactional
@Service
public class AuthServiceImpl implements AuthService {
    public final UserService userService;
    public final PasswordEncoder passwordEncoder;
    public final JwtUtil jwtUtil;
    public final RefreshTokenRepository refreshTokenRepository;
    private static final String DUMMY_PASSWORD_HASH = "$2a$10$2b8Q0eM5GiJQG45BiZrLAOy2Rn/UwEnRZdK4PjHcbgKiEqRZLPeV6";

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
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

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = null;
        String passwordHashToCompare = DUMMY_PASSWORD_HASH;

        try {
            user = userService.findByEmail(loginRequestDto.getEmail());
            passwordHashToCompare = user.getPassword();
        } catch (ResourceNotFoundException ignored) {

        }

        boolean passwordsMatch = passwordEncoder.matches(loginRequestDto.getPassword(), passwordHashToCompare);

        if( user == null || !passwordsMatch) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole());
        String refreshToken = createAndSaveRefreshToken(user.getEmail());

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash); // or use Hex encoding if preferred
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private String createAndSaveRefreshToken(String email) {
        String refreshToken = jwtUtil.generateRefreshToken(email);

        // Hash the refresh token before saving it to the database
        String hashedRefreshToken = hashToken(refreshToken);

        Date expirationDate = jwtUtil.extractExpiration(refreshToken);
        LocalDateTime expiration = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        RefreshToken tokenToSave = RefreshToken.builder()
                .expirationTime(expiration)
                .token(hashedRefreshToken)
                .userEmail(email)
                .build();

        refreshTokenRepository.save(tokenToSave);
        return refreshToken;
    }


    public RefreshTokenDto refreshAccessToken(String refreshToken) {
        jwtUtil.validateToken(refreshToken);
        String email = jwtUtil.extractEmailFromRefreshToken(refreshToken);
        User user = userService.findByEmail(email);

        RefreshToken storedRefreshToken = refreshTokenRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found for user: " + email));


        String hashedRefreshToken = hashToken(refreshToken);
        if (!MessageDigest.isEqual(hashedRefreshToken.getBytes(), storedRefreshToken.getToken().getBytes())) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        // Generate a new access token
        String newAccessToken = jwtUtil.generateToken(email, user.getRole());
        // Delete the old refresh token
        refreshTokenRepository.delete(storedRefreshToken);
        refreshTokenRepository.flush();
        // Create and save a new refresh token
        String newRefreshToken = createAndSaveRefreshToken(email);

        return RefreshTokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void logout(String refreshToken) {
        String email = jwtUtil.extractEmailFromRefreshToken(refreshToken);
        User user = userService.findByEmail(email);

        refreshTokenRepository.deleteByUserEmail(email);
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
