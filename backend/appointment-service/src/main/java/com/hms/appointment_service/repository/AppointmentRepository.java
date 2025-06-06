package com.hms.appointment_service.repository;

import com.hms.appointment_service.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Appointment a " +
            "WHERE a.departmentId = :departmentId " +
            "AND a.appointmentDate = :appointmentDate " +
            "AND a.appointmentTime < :appointmentEndTime " +
            "AND a.appointmentEndTime > :appointmentTime")
    boolean hasConflictingAppointment(@Param("departmentId") String departmentId,
                                      @Param("appointmentDate") LocalDate appointmentDate,
                                      @Param("appointmentTime") LocalTime appointmentTime,
                                      @Param("appointmentEndTime") LocalTime appointmentEndTime);

    long deleteAppointmentById(UUID id);
}
