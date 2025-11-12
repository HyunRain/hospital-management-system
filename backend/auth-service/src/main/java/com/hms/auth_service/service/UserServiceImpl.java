package com.hms.auth_service.service;

import com.hms.auth_service.dto.RegistrationRequestDto;
import com.hms.auth_service.exception.ResourceNotFoundException;
import com.hms.auth_service.model.User;
import com.hms.auth_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User registerUser(RegistrationRequestDto registrationRequestDto, String encodedPassword) {
        User userToSave = User.builder()
                .email(registrationRequestDto.getEmail())
                .password(encodedPassword)
                .role(registrationRequestDto.getRole())
                .build();

        return userRepository.save(userToSave);
    }

}
