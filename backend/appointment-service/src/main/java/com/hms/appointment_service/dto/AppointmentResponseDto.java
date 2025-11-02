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
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalTime appointmentEndTime;
    private AppointmentStatus appointmentStatus;
    private AppointmentType appointmentType;
    private String reason;
}
