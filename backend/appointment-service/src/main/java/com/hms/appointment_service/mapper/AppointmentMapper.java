package com.hms.appointment_service.mapper;

import com.hms.appointment_service.dto.AppointmentRequestDto;
import com.hms.appointment_service.dto.AppointmentResponseDto;
import com.hms.appointment_service.model.Appointment;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AppointmentMapper {
    public AppointmentResponseDto entityToDto(Appointment appointment) {
        return AppointmentResponseDto.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .departmentId(appointment.getDepartmentId())
                .appointmentType(appointment.getAppointmentType())
                .appointmentStatus(appointment.getAppointmentStatus())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .appointmentEndDate(appointment.getAppointmentEndDate())
                .appointmentEndTime(appointment.getAppointmentEndTime())
                .reason(appointment.getReason())
                .build();
    }

    public Appointment dtoToEntity(AppointmentRequestDto appointmentRequestDto) {
        return Appointment.builder()
                .patientId(appointmentRequestDto.getPatientId())
                .doctorId(appointmentRequestDto.getDoctorId())
                .departmentId(appointmentRequestDto.getDepartmentId())
                .appointmentType(appointmentRequestDto.getAppointmentType())
                .appointmentStatus(appointmentRequestDto.getAppointmentStatus())
                .appointmentDate(appointmentRequestDto.getAppointmentDate())
                .appointmentTime(appointmentRequestDto.getAppointmentTime())
                .appointmentEndDate(appointmentRequestDto.getAppointmentEndDate())
                .appointmentEndTime(appointmentRequestDto.getAppointmentEndTime())
                .reason(appointmentRequestDto.getReason())
                .build();
    }

    public Appointment updateEntityFromDto(Appointment existingAppointment, AppointmentRequestDto appointmentRequestDto) {
        if (!Objects.equals(existingAppointment.getPatientId(), appointmentRequestDto.getPatientId())) {
            existingAppointment.setPatientId(appointmentRequestDto.getPatientId());
        }

        if (!Objects.equals(existingAppointment.getDoctorId(), appointmentRequestDto.getDoctorId())) {
            existingAppointment.setDoctorId(appointmentRequestDto.getDoctorId());
        }

        if (!Objects.equals(existingAppointment.getDepartmentId(), appointmentRequestDto.getDepartmentId())) {
            existingAppointment.setDepartmentId(appointmentRequestDto.getDepartmentId());
        }

        if (!Objects.equals(existingAppointment.getAppointmentType(), appointmentRequestDto.getAppointmentType())) {
            existingAppointment.setAppointmentType(appointmentRequestDto.getAppointmentType());
        }

        if (!Objects.equals(existingAppointment.getAppointmentStatus(), appointmentRequestDto.getAppointmentStatus())) {
            existingAppointment.setAppointmentStatus(appointmentRequestDto.getAppointmentStatus());
        }

        if (!Objects.equals(existingAppointment.getAppointmentDate(), appointmentRequestDto.getAppointmentDate())) {
            existingAppointment.setAppointmentDate(appointmentRequestDto.getAppointmentDate());
        }

        if (!Objects.equals(existingAppointment.getAppointmentEndDate(), appointmentRequestDto.getAppointmentEndDate())) {
            existingAppointment.setAppointmentEndDate(appointmentRequestDto.getAppointmentEndDate());
        }

        if (!Objects.equals(existingAppointment.getAppointmentTime(), appointmentRequestDto.getAppointmentTime())) {
            existingAppointment.setAppointmentTime(appointmentRequestDto.getAppointmentTime());
        }

        if (!Objects.equals(existingAppointment.getAppointmentEndTime(), appointmentRequestDto.getAppointmentEndTime())) {
            existingAppointment.setAppointmentEndTime(appointmentRequestDto.getAppointmentEndTime());
        }

        if (!Objects.equals(existingAppointment.getReason(), appointmentRequestDto.getReason())) {
            existingAppointment.setReason(appointmentRequestDto.getReason());
        }

        return existingAppointment;
    }

}
