package com.skillsync.admin.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillsync.activity.repository.ActivityLogRepository;
import com.skillsync.admin.dto.AdminStatsDTO;
import com.skillsync.admin.dto.AdminUserDTO;
import com.skillsync.admin.service.AdminService;
import com.skillsync.common.constant.Role;
import com.skillsync.common.exception.BadRequestException;
import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.skill.category.repository.SkillCategoryRepository;
import com.skillsync.skill.progress.repository.UserSkillProgressRepository;
import com.skillsync.skill.repository.SkillRepository;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final Logger log =
            LoggerFactory.getLogger(AdminServiceImpl.class);

    private final UserRepository              userRepository;
    private final UserSkillProgressRepository progressRepository;
    private final ActivityLogRepository       activityLogRepository;
    private final SkillRepository             skillRepository;
    private final SkillCategoryRepository     categoryRepository;

    @Override
    public Page<AdminUserDTO> getAllUsers(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(sortBy).ascending());

        Page<User> users = userRepository.findAll(pageable);

        return users.map(user -> {
            long   skillsTracked   = progressRepository.countByUser(user);
            double avgProgress     = progressRepository.avgProgressByUser(user);
            long   totalActivities = activityLogRepository.countByUser(user);

            return AdminUserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .createdAt(user.getCreatedAt() != null
                            ? user.getCreatedAt().toString() : "N/A")
                    .skillsTracked((int) skillsTracked)
                    .averageProgress(Math.round(avgProgress * 10.0) / 10.0)
                    .totalActivities((int) totalActivities)
                    .build();
        });
    }

    @Override
    public AdminStatsDTO getPlatformStats() {

        long   totalUsers      = userRepository.count();
        long   totalSkills     = skillRepository.count();
        long   totalCategories = categoryRepository.count();
        long   totalActivities = activityLogRepository.count();
        double avgProgress     = progressRepository.platformAverageProgress();

        // FIX: was activityLogRepository.findAll().stream()... which loads every
        // row into memory. Now uses a single DB aggregate query.
        Long totalMinutes = activityLogRepository.sumAllTimeSpent();

        return AdminStatsDTO.builder()
                .totalUsers(totalUsers)
                .totalSkills(totalSkills)
                .totalCategories(totalCategories)
                .totalActivitiesLogged(totalActivities)
                .totalMinutesPracticed(totalMinutes != null ? totalMinutes : 0L)
                .platformAverageProgress(Math.round(avgProgress * 10.0) / 10.0)
                .build();
    }

    @Override
    @Transactional
    public void promoteToAdmin(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found", "USER_NOT_FOUND"));

        if (user.getRole() == Role.ADMIN) {
            throw new BadRequestException(
                    "User is already an admin", "ALREADY_ADMIN");
        }

        user.setRole(Role.ADMIN);
        userRepository.save(user);
        log.info("User {} promoted to ADMIN", user.getEmail());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found", "USER_NOT_FOUND"));

        // Child records (ActivityLog, UserSkillProgress, RoadmapStep) are
        // deleted first to avoid FK constraint violations.
        // This relies on the cascade = CascadeType.ALL set on User relationships.
        // See User entity for cascade config.
        userRepository.delete(user);
        log.info("User {} deleted by admin", user.getEmail());
    }
}
