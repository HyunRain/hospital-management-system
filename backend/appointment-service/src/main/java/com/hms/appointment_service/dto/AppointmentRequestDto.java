package com.hms.appointment_service.dto;

import com.hms.appointment_service.enums.AppointmentStatus;
import com.hms.appointment_service.enums.AppointmentType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AppointmentRequestDto {
    @NotBlank
    private String patientId;
    @NotBlank
    private String doctorId;
    @NotBlank
    private String departmentId;
    @NotNull
    @FutureOrPresent
    private LocalDate appointmentDate;
    @NotNull
    private LocalTime appointmentTime;
    @NotNull
    private AppointmentStatus appointmentStatus;
    @NotNull
    private AppointmentType appointmentType;
    @NotBlank
    private String reason;
}
