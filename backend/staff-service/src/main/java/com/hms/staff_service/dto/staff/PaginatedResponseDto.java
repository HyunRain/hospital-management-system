package com.hms.staff_service.dto.staff;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedResponseDto {
    private List<StaffResponseDto> staffResponseDtos;
    private int totalPages;
    private long totalStaff;
}
