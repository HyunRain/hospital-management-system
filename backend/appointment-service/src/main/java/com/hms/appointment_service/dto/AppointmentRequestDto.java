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
import java.util.UUID;

@Data
@Builder
public class AppointmentRequestDto {
    private UUID id;
    @NotBlank
    private String patientId;
    @NotBlank
    private String doctorId;
    @NotBlank
    private String departmentId;
    @NotNull
    @FutureOrPresent
    private LocalDate appointmentDate;
    @FutureOrPresent
    private LocalDate appointmentEndDate;
    @NotNull
    private LocalTime appointmentTime;
    @NotNull
    private LocalTime appointmentEndTime;
    @NotNull
    private AppointmentStatus appointmentStatus;
    @NotNull
    private AppointmentType appointmentType;
    @NotBlank
    private String reason;
}
