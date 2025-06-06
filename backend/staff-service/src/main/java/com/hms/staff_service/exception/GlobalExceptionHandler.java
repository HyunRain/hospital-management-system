package com.hms.staff_service.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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

    @ExceptionHandler(DepartmentAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleDepartmentAlreadyExistsException(DepartmentAlreadyExistsException ex) {
        log.warn("Department already exists {}", ex.getMessage());
        Map<String,String> errors = new HashMap<>();
        errors.put("message", "Department already exists");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleDepartmentNotFoundException(DepartmentNotFoundException ex) {
        log.warn("Department not found {}", ex.getMessage());
        Map<String,String> errors = new HashMap<>();
        errors.put("message", "Department not found");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        log.warn("Email already exists {}", ex.getMessage());
        Map<String,String> errors = new HashMap<>();
        errors.put("message", "Email already exists");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleEmailAlreadyExistsException(ResourceNotFoundException ex) {
        log.warn("Email not found {}", ex.getMessage());
        Map<String,String> errors = new HashMap<>();
        errors.put("message", "Email not found");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Duplicate entry {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Duplicate entry or constraint violation");
        return ResponseEntity.badRequest().body(errors);
    }
}