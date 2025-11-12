package com.hms.patient_service.controller;

import com.hms.patient_service.dto.PaginatedResponse;
import com.hms.patient_service.dto.PatientDto;
import com.hms.patient_service.dto.SimplePatientDto;
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
        System.out.println("test123");
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
    public ResponseEntity<PaginatedResponse> getAllPatients(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        PaginatedResponse allPatientsPaginated = patientService.getAllPatientsPaginated(page, size);
        return new ResponseEntity<>(allPatientsPaginated, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Patients by Fields")
    public ResponseEntity<PaginatedResponse> searchPatients(@RequestParam String input, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size ) {
        PaginatedResponse allPatientsPaginated = patientService.searchPatients(input, page, size);
        return new ResponseEntity<>(allPatientsPaginated, HttpStatus.OK);
    }

    @PostMapping("/batch")
    @Operation(summary = "Get Patients by a list of Patient IDs")
    public ResponseEntity<List<SimplePatientDto>> getBatchOfPatientsByIds(@RequestBody List<String> patientIds) {
        List<SimplePatientDto> patients = patientService.getPatientsByIds(patientIds);
        return new ResponseEntity<>(patients, HttpStatus.OK);
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
