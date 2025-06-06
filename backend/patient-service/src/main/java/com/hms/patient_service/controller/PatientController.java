package com.hms.patient_service.controller;

import com.hms.patient_service.dto.PatientDto;
import com.hms.patient_service.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
@Tag(name = "Patient", description = "API Patient Service")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    @Operation(summary = "Save a patient")
    public ResponseEntity<PatientDto> savePatient(@Valid @RequestBody PatientDto patientDto) {
        PatientDto savedPatient = patientService.savePatient(patientDto);
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    @GetMapping("/get/{patientId}")
    @Operation(summary = "Get a patient")
    public ResponseEntity<PatientDto> getPatientByPatientId(@PathVariable String patientId) {
        PatientDto patientById = patientService.getPatientById(patientId);
        return new ResponseEntity<>(patientById, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all patients")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> allPatients = patientService.getAllPatients();
        return new ResponseEntity<>(allPatients, HttpStatus.OK);
    }

    @PatchMapping("/update/{patientId}")
    @Operation(summary = "Update a patient")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable String patientId, @Valid @RequestBody PatientDto patientDto) {
        PatientDto updatedPatient = patientService.updatePatient(patientId, patientDto);
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{patientId}")
    @Operation(summary = "Delete a patient")
    public ResponseEntity<Void> deletePatient(@PathVariable String patientId) {
        patientService.deletePatient(patientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
