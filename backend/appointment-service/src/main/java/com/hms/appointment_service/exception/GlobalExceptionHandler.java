package com.hms.appointment_service.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(AppointmentConflictException.class)
    public ResponseEntity<Map<String,String>> handleAppointmentConflictException(AppointmentConflictException ex) {
        log.warn("Appointment conflict {}", ex.getMessage());
        Map<String,String> errors = new HashMap<>();
        errors.put("message", "Appointment conflict");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Resource not found {}", ex.getMessage());
        Map<String,String> errors = new HashMap<>();
        errors.put("message", "Resource not found");
        return ResponseEntity.badRequest().body(errors);
    }

}
