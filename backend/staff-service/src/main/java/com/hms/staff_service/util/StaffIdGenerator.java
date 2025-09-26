package com.hms.staff_service.util;

import com.hms.staff_service.repository.StaffRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class StaffIdGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ID_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();
    private final StaffRepository staffRepository;

    public StaffIdGenerator(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    private String generateStaffId() {
        StringBuilder staffId = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            staffId.append(CHARACTERS.charAt(index));
        }
        return staffId.toString();
    }

    public String generateUniqueStaffId() {
        String staffId;

        do {
            staffId = generateStaffId();
        } while(staffRepository.existsByStaffId(staffId));

        return staffId;
    }
}
