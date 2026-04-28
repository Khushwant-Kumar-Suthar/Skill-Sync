package com.skillsync.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillsync.common.exception.BadRequestException;
import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.user.dto.ChangePasswordRequest;
import com.skillsync.user.dto.UpdateProfileRequest;
import com.skillsync.user.dto.UserProfileDTO;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;
import com.skillsync.user.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileDTO getProfile() {
        User user = getCurrentUser();
        log.info("Fetching profile for: {}", user.getEmail());
        return toDTO(user);
    }

    @Override
    @Transactional
    public UserProfileDTO updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();

        user.setName(request.getName());
        userRepository.save(user);

        log.info("Profile updated for: {}", user.getEmail());
        return toDTO(user);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();

        // Verify current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(),
                user.getPassword())) {
            throw new BadRequestException(
                    "Current password is incorrect", "INVALID_PASSWORD");
        }

        // Verify new password and confirm match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException(
                    "New password and confirm password do not match",
                    "PASSWORD_MISMATCH");
        }

        // Prevent reusing the same password
        if (passwordEncoder.matches(request.getNewPassword(),
                user.getPassword())) {
            throw new BadRequestException(
                    "New password must be different from the current password",
                    "SAME_PASSWORD");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("Password changed successfully for: {}", user.getEmail());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found", "USER_NOT_FOUND"));
    }

    private UserProfileDTO toDTO(User user) {
        return UserProfileDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
