package com.hms.staff_service.repository;

import java.util.UUID;

public interface DepartmentStaffCount {
    UUID getDepartmentId();
    Integer getStaffCount();
}
