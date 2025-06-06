package com.hms.appointment_service.service;

import com.hms.appointment_service.dto.AppointmentRequestDto;
import com.hms.appointment_service.dto.AppointmentResponseDto;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto);

    AppointmentResponseDto getAppointmentById(UUID id);

    List<AppointmentResponseDto> getAllAppointments();

    AppointmentResponseDto updateAppointment(UUID id, AppointmentRequestDto appointmentRequestDto);

    void deleteAppointment(UUID id);
}
