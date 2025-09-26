package com.hms.staff_service.repository;

import com.hms.staff_service.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    boolean existsByName(String name);
    Optional<Department> findByName(String name);

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.headOfDepartment")
    List<Department> findAllWithHeads();

}
