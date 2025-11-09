package com.hms.appointment_service.service;

import com.hms.appointment_service.clients.PatientClient;
import com.hms.appointment_service.clients.StaffClient;
import com.hms.appointment_service.dto.AppointmentRequestDto;
import com.hms.appointment_service.dto.AppointmentResponseDto;
import com.hms.appointment_service.dto.DoctorDto;
import com.hms.appointment_service.dto.PatientDto;
import com.hms.appointment_service.exception.AppointmentConflictException;
import com.hms.appointment_service.exception.ResourceNotFoundException;
import com.hms.appointment_service.mapper.AppointmentMapper;
import com.hms.appointment_service.model.Appointment;
import com.hms.appointment_service.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientClient patientClient;
    private final StaffClient staffClient;

    @Override
    public AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto) {
        boolean isConflicting = appointmentRepository.hasConflictingAppointment(
                appointmentRequestDto.getDepartmentId(),
                appointmentRequestDto.getDoctorId(),
                appointmentRequestDto.getAppointmentDate(),
                appointmentRequestDto.getAppointmentTime(),
                appointmentRequestDto.getAppointmentEndTime()
        );

        if (isConflicting)
            throw new AppointmentConflictException("Appointment time conflicts with an existing appointment.");


        Appointment appointmentToSave = appointmentMapper.dtoToEntity(appointmentRequestDto);
        Appointment savedAppointment = appointmentRepository.save(appointmentToSave);

        PatientDto patientName = patientClient.getPatientNames(List.of(savedAppointment.getPatientId()))
                .flatMapMany(Flux::fromIterable)
                .next()
                .block();

        DoctorDto doctorName = staffClient.getDoctorNames((List.of(appointmentToSave.getDoctorId())))
                .flatMapMany(Flux::fromIterable)
                .next()
                .block();

        AppointmentResponseDto appointmentResponseDto = appointmentMapper.entityToDto(savedAppointment);

        if(patientName == null || doctorName == null) {
            return appointmentResponseDto;
        }

        appointmentResponseDto.setPatientName(patientName.getFirstName() + " " + patientName.getLastName());
        appointmentResponseDto.setDoctorName(doctorName.getFirstName() + " " + doctorName.getLastName());
        return appointmentResponseDto;
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
    public List<AppointmentResponseDto> getAppointmentsByMonthRange(int year, int month) {
        LocalDate startMonth = LocalDate.of(year, month, 1);
        LocalDate endMonth = startMonth.withDayOfMonth(startMonth.lengthOfMonth());

        List<Appointment> appointments = appointmentRepository.findByAppointmentDateBetween(startMonth, endMonth);

        List<String> patientIds = appointments.stream().map(Appointment::getPatientId).distinct().toList();
        Map<String, String> patientMap = patientClient.getPatientNames(patientIds)
                .flatMapMany(Flux::fromIterable)
                .collectMap(PatientDto::getPatientId, patientDto -> patientDto.getFirstName() + " " + patientDto.getLastName())
                .block();

        List<String> doctorIds = appointments.stream().map(Appointment::getDoctorId).distinct().toList();
        Map<String, String> doctorMap = staffClient.getDoctorNames(doctorIds)
                .flatMapMany(Flux::fromIterable)
                .collectMap(DoctorDto::getDoctorId, doctorDto -> doctorDto.getFirstName() + " " + doctorDto.getLastName())
                .block();

        if(patientMap == null || doctorMap == null) {
            return appointments.stream().map(appointmentMapper::entityToDto).toList();
        }

        return appointments.stream().map(appointment -> {
            AppointmentResponseDto dto = appointmentMapper.entityToDto(appointment);
            dto.setPatientName(patientMap.get(appointment.getPatientId()));
            dto.setDoctorName(doctorMap.get(appointment.getDoctorId()));
            return dto;
        }).toList();
    }

    @Override
    public AppointmentResponseDto updateAppointment(UUID id, AppointmentRequestDto appointmentRequestDto) {
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        boolean isConflicting = appointmentRepository.hasConflictingAppointment(
                appointmentRequestDto.getDepartmentId(),
                appointmentRequestDto.getDoctorId(),
                appointmentRequestDto.getAppointmentDate(),
                appointmentRequestDto.getAppointmentTime(),
                appointmentRequestDto.getAppointmentTime().plusMinutes(30)
        );

        if (isConflicting)
            throw new AppointmentConflictException("Appointment time conflicts with an existing appointment.");

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
