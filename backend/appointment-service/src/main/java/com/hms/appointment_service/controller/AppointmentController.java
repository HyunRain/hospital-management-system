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

import java.time.LocalDate;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentResponseDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable UUID id) {
        AppointmentResponseDto appointmentResponseDto = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointmentResponseDto);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        List<AppointmentResponseDto> allAppointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(allAppointments);
    }

    @GetMapping("/monthRange/{year}/{month}")
    @Operation(summary = "Get all appointments in current/previous/upcoming month")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByMonthRange(@PathVariable int year, @PathVariable int month) {
        List<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByMonthRange(year, month);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/count")
    @Operation(summary = "Get the count of all upcoming events")
    public ResponseEntity<Long> getAppointmentCount() {
        Long appointmentCount = appointmentService.countAppointments();
        return ResponseEntity.ok(appointmentCount);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing appointment")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(@PathVariable UUID id, @Valid @RequestBody AppointmentRequestDto appointmentRequestDto) {
        AppointmentResponseDto updatedAppointment = appointmentService.updateAppointment(id, appointmentRequestDto);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an appointment by ID")
    public ResponseEntity<String> deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Appointment deleted successfully");
    }
}
