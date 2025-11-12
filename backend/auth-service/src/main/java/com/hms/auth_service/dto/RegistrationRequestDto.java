package com.hms.auth_service.dto;

import com.hms.auth_service.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email needs to be valid")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    @NotBlank(message = "Repeated password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String repeatedPassword;
    @NotNull(message = "Role is required")
    private Role role;
}
