package com.hms.staff_service.repository;

import com.hms.staff_service.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {
    boolean existsByEmail(String email);
    Optional<Staff> findByEmail(String email);
    long deleteByEmail(String email);
}
