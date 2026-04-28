package com.skillsync.roadmap.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillsync.common.exception.BadRequestException;
import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.roadmap.dto.GenerateRoadmapRequest;
import com.skillsync.roadmap.dto.RoadmapResponse;
import com.skillsync.roadmap.dto.RoadmapStepDTO;
import com.skillsync.roadmap.entity.RoadmapStep;
import com.skillsync.roadmap.generator.RoadmapGenerator;
import com.skillsync.roadmap.repository.RoadmapStepRepository;
import com.skillsync.roadmap.service.RoadmapService;
import com.skillsync.skill.progress.entity.UserSkillProgress;
import com.skillsync.skill.progress.repository.UserSkillProgressRepository;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoadmapServiceImpl implements RoadmapService {

    private static final Logger log =
            LoggerFactory.getLogger(RoadmapServiceImpl.class);

    private final UserRepository              userRepository;
    private final UserSkillProgressRepository progressRepository;
    private final RoadmapStepRepository       roadmapStepRepository;
    private final RoadmapGenerator            roadmapGenerator;

    // ── Public API ────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public RoadmapResponse getRoadmap() {

        User user = getCurrentUser();
        log.info("Fetching roadmap for user: {}", user.getEmail());

        List<UserSkillProgress> progressList =
                progressRepository.findByUserWithSkill(user);

        List<RoadmapStep> steps =
                roadmapStepRepository.findByUserOrderByStepOrder(user);

        boolean roadmapIsStale = isRoadmapStale(steps, progressList);

        if (steps.isEmpty() || roadmapIsStale) {
            if (!progressList.isEmpty()) {
                log.info("Regenerating roadmap for user: {} (stale={})",
                        user.getEmail(), roadmapIsStale);
                roadmapStepRepository.deleteAllByUser(user);
                // Default 60 min/day when auto-generating
                steps = roadmapGenerator.generate(user, progressList, 60);
                roadmapStepRepository.saveAll(steps);
            }
        }

        return buildResponse(user, steps);
    }

    @Override
    @Transactional
    public RoadmapResponse generateRoadmap(GenerateRoadmapRequest request) {

        User user = getCurrentUser();
        log.info("Generating roadmap for user: {} with {} min/day",
                user.getEmail(), request.getDailyMinutesAvailable());

        List<UserSkillProgress> progressList =
                progressRepository.findByUserWithSkill(user);

        if (progressList.isEmpty()) {
            throw new BadRequestException(
                    "No skills tracked yet. Start tracking skills before generating a roadmap.",
                    "NO_SKILLS_TRACKED");
        }

        // Always delete and regenerate fresh when explicitly requested
        roadmapStepRepository.deleteAllByUser(user);

        int dailyMinutes = request.getDailyMinutesAvailable() != null
                ? request.getDailyMinutesAvailable() : 60;

        List<RoadmapStep> steps =
                roadmapGenerator.generate(user, progressList, dailyMinutes);
        roadmapStepRepository.saveAll(steps);

        log.info("Generated {} roadmap steps for user: {}",
                steps.size(), user.getEmail());

        return buildResponse(user, steps);
    }

    @Override
    @Transactional
    public void markStepCompleted(Long stepId) {

        User user = getCurrentUser();

        RoadmapStep step = roadmapStepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Roadmap step not found", "STEP_NOT_FOUND"));

        if (!step.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException(
                    "Roadmap step not found", "STEP_NOT_FOUND");
        }

        if (Boolean.TRUE.equals(step.getCompleted())) {
            throw new BadRequestException(
                    "Step is already marked as completed",
                    "STEP_ALREADY_COMPLETED");
        }

        step.setCompleted(true);
        roadmapStepRepository.save(step);
        log.info("Step {} marked as completed for user: {}",
                stepId, user.getEmail());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * A roadmap is stale when the user has incomplete skills that are not
     * represented in any existing roadmap step. This happens when a user
     * starts tracking a new skill after their roadmap was first generated.
     */
    private boolean isRoadmapStale(List<RoadmapStep> steps,
                                   List<UserSkillProgress> progressList) {
        if (steps.isEmpty()) return false;

        Set<Long> roadmapSkillIds = steps.stream()
                .map(s -> s.getSkill().getId())
                .collect(Collectors.toSet());

        return progressList.stream()
                .filter(p -> p.getProgressPercentage() < 100.0)
                .anyMatch(p -> !roadmapSkillIds.contains(p.getSkill().getId()));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found", "USER_NOT_FOUND"));
    }

    private RoadmapResponse buildResponse(User user, List<RoadmapStep> steps) {

        List<RoadmapStepDTO> stepDTOs = steps.stream()
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

        long completedCount = steps.stream()
                .filter(s -> Boolean.TRUE.equals(s.getCompleted()))
                .count();

        double overallProgress = steps.isEmpty() ? 0.0
                : Math.round((completedCount * 100.0 / steps.size()) * 10.0) / 10.0;

        return RoadmapResponse.builder()
                .userName(user.getName())
                .totalSteps(steps.size())
                .completedSteps((int) completedCount)
                .overallProgressPercent(overallProgress)
                .steps(stepDTOs)
                .build();
    }
}