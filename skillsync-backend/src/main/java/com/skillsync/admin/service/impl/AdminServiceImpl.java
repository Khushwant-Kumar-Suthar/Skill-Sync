package com.skillsync.admin.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillsync.activity.repository.ActivityLogRepository;
import com.skillsync.admin.dto.AdminRoadmapDTO;
import com.skillsync.admin.dto.AdminSkillProgressDTO;
import com.skillsync.admin.dto.AdminStatsDTO;
import com.skillsync.admin.dto.AdminUserDTO;
import com.skillsync.admin.service.AdminService;
import com.skillsync.common.constant.Role;
import com.skillsync.common.exception.BadRequestException;
import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.roadmap.dto.RoadmapStepDTO;
import com.skillsync.roadmap.entity.RoadmapStep;
import com.skillsync.roadmap.repository.RoadmapStepRepository;
import com.skillsync.skill.category.repository.SkillCategoryRepository;
import com.skillsync.skill.progress.entity.UserSkillProgress;
import com.skillsync.skill.progress.repository.UserSkillProgressRepository;
import com.skillsync.skill.repository.SkillRepository;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

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
    private final RoadmapStepRepository       roadmapStepRepository;

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
    @Transactional(readOnly = true)
    public List<AdminRoadmapDTO> getAllRoadmaps() {
        List<RoadmapStep> steps = roadmapStepRepository.findAllForAdmin();

        Map<User, List<RoadmapStep>> stepsByUser = steps.stream()
                .collect(Collectors.groupingBy(RoadmapStep::getUser));

        return stepsByUser.entrySet().stream()
                .sorted((a, b) -> a.getKey().getName().compareToIgnoreCase(b.getKey().getName()))
                .map(entry -> buildAdminRoadmap(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminSkillProgressDTO> getAllSkillProgress() {
        List<UserSkillProgress> progress = progressRepository.findAllForAdmin();

        return progress.stream()
                .map(p -> AdminSkillProgressDTO.builder()
                        .userId(p.getUser().getId())
                        .userName(p.getUser().getName())
                        .userEmail(p.getUser().getEmail())
                        .skillId(p.getSkill().getId())
                        .skillName(p.getSkill().getName())
                        .categoryName(p.getSkill().getCategory().getName())
                        .progressPercentage(round(p.getProgressPercentage()))
                        .score(round(p.getScore()))
                        .lastPracticedAt(p.getLastPracticedAt() != null
                                ? p.getLastPracticedAt().toString()
                                : null)
                        .build())
                .toList();
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

    private AdminRoadmapDTO buildAdminRoadmap(User user, List<RoadmapStep> steps) {
        List<RoadmapStepDTO> stepDTOs = steps.stream()
                .sorted((a, b) -> a.getStepOrder().compareTo(b.getStepOrder()))
                .map(s -> RoadmapStepDTO.builder()
                        .stepId(s.getId())
                        .stepOrder(s.getStepOrder())
                        .skillId(s.getSkill().getId())
                        .skillName(s.getSkill().getName())
                        .title(s.getTitle())
                        .description(s.getDescription())
                        .estimatedDays(s.getEstimatedDays())
                        .completed(s.getCompleted())
                        .build())
                .toList();

        long completed = steps.stream()
                .filter(s -> Boolean.TRUE.equals(s.getCompleted()))
                .count();

        double overallProgress = steps.isEmpty() ? 0.0
                : round(completed * 100.0 / steps.size());

        return AdminRoadmapDTO.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .totalSteps(steps.size())
                .completedSteps((int) completed)
                .overallProgressPercent(overallProgress)
                .steps(stepDTOs)
                .build();
    }

    private double round(Double value) {
        if (value == null) return 0.0;
        return Math.round(value * 10.0) / 10.0;
    }
}
