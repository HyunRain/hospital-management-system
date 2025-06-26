package com.hms.staff_service.repository;

import com.hms.staff_service.enums.Role;
import com.hms.staff_service.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID>, JpaSpecificationExecutor<Staff> {
    boolean existsByEmail(String email);
    boolean existsByStaffId(String staffId);
    Optional<Staff> findByEmail(String email);
    long deleteByEmail(String email);

    @Query("SELECT s.uuid FROM Staff s WHERE s.role = :role ORDER BY s.uuid ASC")
    Page<UUID> findPagedRoleIds(@Param("role") Role role, Pageable pageable);

    @Query("SELECT s FROM Staff s JOIN FETCH s.department WHERE s.uuid IN :uuids")
    List<Staff> findStaffWithDepartmentByIds(@Param("uuids") List<UUID> uuids);
}
