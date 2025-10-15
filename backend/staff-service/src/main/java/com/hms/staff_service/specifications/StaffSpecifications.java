package com.hms.staff_service.specifications;

import com.hms.staff_service.model.Staff;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class StaffSpecifications {
    public static Specification<Staff> staffContainsTerm(String input, Boolean doctorSearch) {
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

            Predicate generalPredicate = cb.or(
                    namePredicate,
                    cb.like(cb.lower(root.get("email")), likeInput),
                    cb.like(cb.lower(root.get("city")), likeInput),
                    cb.like(cb.lower(root.get("staffId")), likeInput),
                    cb.like(cb.lower(root.join("department").get("name")), likeInput),
                    cb.like(cb.lower(root.get("gender").as(String.class)), likeInput),
                    cb.like(cb.lower(cb.function("TO_CHAR", String.class, root.get("dateOfBirth"), cb.literal("YYYY-MM-DD"))), likeInput)
            );

            if (Boolean.TRUE.equals(doctorSearch)) {
                Predicate doctorPredicate = cb.equal(root.get("role"), "DOCTOR");
                return cb.and(generalPredicate, doctorPredicate);
            } else if (Boolean.FALSE.equals(doctorSearch)) {
                Predicate nonDoctorPredicate = cb.notEqual(root.get("role"), "DOCTOR");
                return cb.and(generalPredicate, nonDoctorPredicate);
            }

            return generalPredicate;
        };
    }
}

