package com.hms.appointment_service.dto;

import com.hms.appointment_service.enums.AppointmentStatus;
import com.hms.appointment_service.enums.AppointmentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AppointmentResponseDto {
    private String patientId;
    private String doctorId;
    private String departmentId;
    private LocalDate appointmentDate; // Format: YYYY-MM-DD
    private LocalTime appointmentTime; // Format: HH:mm
    private LocalTime appointmentEndTime; // Format: HH:mm, must be after appointmentTime
    private AppointmentStatus appointmentStatus; // e.g., "Scheduled", "Cancelled", "Completed"
    private AppointmentType appointmentType; // e.g., "In-person", "Telemedicine"
    private String reason;
}
