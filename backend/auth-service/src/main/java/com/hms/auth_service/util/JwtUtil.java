package com.hms.auth_service.util;

import com.hms.auth_service.dto.LoginInfoResponseDto;
import com.hms.auth_service.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key secretKey;
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15; // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24; // 1 day

    public JwtUtil(@Value("${jwt.secret}") String secret) {
         byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
         this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, Role role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }


    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("type", "refresh")  // mark token type
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(secretKey)
                .compact();
    }

    public void validateToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new JwtException("JWT token is expired");
            }
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature");
        } catch (JwtException e) {
            throw new JwtException("JWT token is expired");
        }
    }

    public LoginInfoResponseDto extractEmailAndRoleFromJwt(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new JwtException("JWT token is expired");
            }

            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            return LoginInfoResponseDto.builder()
                    .email(email)
                    .role(Role.valueOf(role))
                    .build();
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature");
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT");
        }
    }

    public String extractEmailFromRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new JwtException("JWT token is expired");
            }

            return claims.getSubject();
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature");
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT");
        }
    }

    public String extractRoleFromJwt(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                throw new JwtException("JWT token is expired");
            }


            //System.out.println(claims.get("role", String.class));
            return claims.get("role", String.class);
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature");
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT");
        }
    }

    public Date extractExpiration(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration();
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT");
        }
    }
}
