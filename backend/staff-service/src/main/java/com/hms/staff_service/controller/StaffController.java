package com.hms.staff_service.controller;

import com.hms.staff_service.dto.staff.PaginatedResponseDto;
import com.hms.staff_service.dto.staff.StaffRequestDto;
import com.hms.staff_service.dto.staff.StaffResponseDto;
import com.hms.staff_service.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@Tag(name = "Staff", description = "API Staff Service")
@Slf4j
public class StaffController {
    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    @Operation(summary = "Save a Staff")
    public ResponseEntity<StaffResponseDto> registerStaff(@Valid @RequestBody StaffRequestDto staffRequestDto) {
        StaffResponseDto staffResponseDto = staffService.registerStaff(staffRequestDto);
        return new ResponseEntity<>(staffResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("get/{email}")
    @Operation(summary = "Get Staff by Email")
    public ResponseEntity<StaffResponseDto> getStaffByEmail(@PathVariable String email) {
        StaffResponseDto staffResponseDto = staffService.getStaffByEmail(email);
        return new ResponseEntity<>(staffResponseDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Get 15 Staff Paginated")
    public ResponseEntity<PaginatedResponseDto> getAllStaff(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size, @RequestParam String role) {
        PaginatedResponseDto allStaff = staffService.getAllStaffPaginated(page, size, role);
        return new ResponseEntity<>(allStaff, HttpStatus.OK);
    }

    @GetMapping("/allRoles")
    @Operation(summary = "Get 15 Staff Paginated")
    public ResponseEntity<PaginatedResponseDto> getAllStaff(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        PaginatedResponseDto allStaff = staffService.getAllStaffPaginated(page, size );
        return new ResponseEntity<>(allStaff, HttpStatus.OK);
    }



    @GetMapping("/search")
    @Operation(summary = "Search for Doctors")
    public ResponseEntity<PaginatedResponseDto> searchStaff(@RequestParam String input, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        PaginatedResponseDto searchedDoctors = staffService.searchDoctors(input, page, size);
        return new ResponseEntity<>(searchedDoctors, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{email}")
    @Operation(summary = "Delete Staff by Email")
    public ResponseEntity<String> deleteStaffByEmail(@PathVariable String email) {
        staffService.deleteStaffByEmail(email);
        return new ResponseEntity<>("Staff deleted successfully", HttpStatus.OK);
    }
}
