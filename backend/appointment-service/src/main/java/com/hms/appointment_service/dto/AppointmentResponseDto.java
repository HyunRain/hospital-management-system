package com.hms.appointment_service.dto;

import com.hms.appointment_service.enums.AppointmentStatus;
import com.hms.appointment_service.enums.AppointmentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class AppointmentResponseDto {
    private UUID id;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String departmentId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalDate appointmentEndDate;
    private LocalTime appointmentEndTime;
    private AppointmentStatus appointmentStatus;
    private AppointmentType appointmentType;
    private String reason;
}
