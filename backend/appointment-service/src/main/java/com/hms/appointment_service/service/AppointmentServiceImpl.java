package com.hms.appointment_service.service;

import com.hms.appointment_service.dto.AppointmentRequestDto;
import com.hms.appointment_service.dto.AppointmentResponseDto;
import com.hms.appointment_service.exception.AppointmentConflictException;
import com.hms.appointment_service.exception.ResourceNotFoundException;
import com.hms.appointment_service.mapper.AppointmentMapper;
import com.hms.appointment_service.model.Appointment;
import com.hms.appointment_service.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    public AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto) {
        LocalTime appointmentEndTime = appointmentRequestDto.getAppointmentTime().plusMinutes(30);

        boolean isConflicting = appointmentRepository.hasConflictingAppointment(
                appointmentRequestDto.getDepartmentId(),
                appointmentRequestDto.getAppointmentDate(),
                appointmentRequestDto.getAppointmentTime(),
                appointmentEndTime
        );

        if (isConflicting) throw new AppointmentConflictException("Appointment time conflicts with an existing appointment.");


        Appointment appointmentToSave = appointmentMapper.dtoToEntity(appointmentRequestDto);
        appointmentToSave.setAppointmentEndTime(appointmentEndTime);
        Appointment savedAppointment = appointmentRepository.save(appointmentToSave);
        return appointmentMapper.entityToDto(savedAppointment);
    }

    @Override
    public AppointmentResponseDto getAppointmentById(UUID id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        return appointmentMapper.entityToDto(appointment);
    }

    @Override
    public List<AppointmentResponseDto> getAllAppointments() {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        return allAppointments.stream().map(appointmentMapper::entityToDto).toList();
    }

    @Override
    public AppointmentResponseDto updateAppointment(UUID id, AppointmentRequestDto appointmentRequestDto) {
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        boolean isConflicting = appointmentRepository.hasConflictingAppointment(
                appointmentRequestDto.getDepartmentId(),
                appointmentRequestDto.getAppointmentDate(),
                appointmentRequestDto.getAppointmentTime(),
                appointmentRequestDto.getAppointmentTime().plusMinutes(30)
        );

        if (isConflicting) throw new AppointmentConflictException("Appointment time conflicts with an existing appointment.");

        Appointment updatedAppointment = appointmentMapper.updateEntityFromDto(existingAppointment, appointmentRequestDto);
        Appointment savedAppointment = appointmentRepository.save(updatedAppointment);
        return appointmentMapper.entityToDto(savedAppointment);
    }

    @Override
    public void deleteAppointment(UUID id) {
        long deleteCount = appointmentRepository.deleteAppointmentById(id);
        if (deleteCount == 0) throw new ResourceNotFoundException("Appointment not found with ID: " + id);
    }


}
