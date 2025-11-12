package com.hms.auth_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenDto {
    private String accessToken;
    private String refreshToken;
}
