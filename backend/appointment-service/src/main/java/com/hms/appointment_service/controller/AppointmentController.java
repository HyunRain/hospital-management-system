package com.hms.appointment_service.controller;

import com.hms.appointment_service.dto.AppointmentRequestDto;
import com.hms.appointment_service.dto.AppointmentResponseDto;
import com.hms.appointment_service.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
@Tag(name = "Appointment", description = "API Appointment Service")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Create a new appointment")
    public ResponseEntity<AppointmentResponseDto> createAppointment(@Valid @RequestBody AppointmentRequestDto appointmentRequestDto) {
        AppointmentResponseDto appointmentResponseDto = appointmentService.createAppointment(appointmentRequestDto);
        return new ResponseEntity<>(appointmentResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get appointment by ID")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable UUID id) {
        AppointmentResponseDto appointmentResponseDto = appointmentService.getAppointmentById(id);
        return new ResponseEntity<>(appointmentResponseDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        List<AppointmentResponseDto> allAppointments = appointmentService.getAllAppointments();
        return new ResponseEntity<>(allAppointments, HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Update an existing appointment")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(@PathVariable UUID id, @Valid @RequestBody AppointmentRequestDto appointmentRequestDto) {
        AppointmentResponseDto updatedAppointment = appointmentService.updateAppointment(id, appointmentRequestDto);
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete an appointment by ID")
    public ResponseEntity<String> deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return new ResponseEntity<>("Appointment deleted successfully", HttpStatus.NO_CONTENT);
    }
}
