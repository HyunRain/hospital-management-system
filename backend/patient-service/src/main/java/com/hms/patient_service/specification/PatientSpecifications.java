package com.hms.patient_service.specification;

import com.hms.patient_service.model.Patient;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class PatientSpecifications {
    public static Specification<Patient> patientContainsTerm(String input) {
        return (root, query, cb) -> {
            if (input == null || input.isEmpty()) {
                return cb.conjunction(); // no filtering
            }

            String likeInput = "%" + input.trim().toLowerCase() + "%";

            String[] parts = input.split("\\s+");
            Predicate namePredicate;

            if (parts.length >= 2) {
                // If full name like "John Doe", search firstName and lastName
                namePredicate = cb.and(
                        cb.like(cb.lower(root.get("firstName")), "%" + parts[0].trim().toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("lastName")), "%" + parts[1].trim().toLowerCase() + "%")
                );
            } else {
                // If just "John", match first or last name
                namePredicate = cb.or(
                        cb.like(cb.lower(root.get("firstName")), likeInput),
                        cb.like(cb.lower(root.get("lastName")), likeInput)
                );
            }

            return cb.or(
                    namePredicate,
                    cb.like(cb.lower(root.get("email")), likeInput),
                    cb.like(cb.lower(root.get("city")), likeInput),
                    cb.like(cb.lower(root.get("patientId")), likeInput),
                    cb.like(cb.lower(root.get("addressLine1")), likeInput),
                    cb.like(cb.lower(root.get("gender").as(String.class)), likeInput),
                    cb.like(cb.lower(root.get("status").as(String.class)), likeInput),
                    cb.like(cb.lower(root.get("bloodGroup").as(String.class)), likeInput),
                    cb.like(cb.lower(root.get("maritalStatus").as(String.class)), likeInput),
                    cb.like(cb.lower(cb.function("TO_CHAR", String.class, root.get("dateOfBirth"), cb.literal("YYYY-MM-DD"))), likeInput)
            );
        };
    }
}

