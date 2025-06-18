package com.hms.auth_service.dto;

import com.hms.auth_service.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginInfoResponseDto {
    private String email;
    private Role role;
}
