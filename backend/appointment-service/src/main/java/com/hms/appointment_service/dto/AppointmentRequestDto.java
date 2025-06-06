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
    private LocalDate appointmentDate; // Format: YYYY-MM-DD
    @NotNull
    private LocalTime appointmentTime; // Format: HH:mm
    @NotNull
    private AppointmentStatus appointmentStatus; // e.g., "Scheduled", "Cancelled", "Completed"
    @NotNull
    private AppointmentType appointmentType; // e.g., "In-person", "Telemedicine"
    @NotBlank
    private String reason; // Reason for the appointment
}
