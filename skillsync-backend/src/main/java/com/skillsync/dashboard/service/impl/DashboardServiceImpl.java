package com.skillsync.dashboard.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.skillsync.activity.repository.ActivityLogRepository;
import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.dashboard.dto.DashboardResponse;
import com.skillsync.dashboard.service.DashboardService;
import com.skillsync.skill.progress.entity.UserSkillProgress;
import com.skillsync.skill.progress.repository.UserSkillProgressRepository;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private static final Logger log =
            LoggerFactory.getLogger(DashboardServiceImpl.class);

    private final UserRepository              userRepository;
    private final UserSkillProgressRepository progressRepository;
    private final ActivityLogRepository       activityLogRepository;

    @Override
    public DashboardResponse getDashboard() {

        String email = SecurityContextHolder
                .getContext().getAuthentication().getName();

        log.info("Building dashboard for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found", "USER_NOT_FOUND"));

        List<UserSkillProgress> progressList =
                progressRepository.findByUserWithSkill(user);

        // ── Skill counts ──────────────────────────────────────────────────────
        int mastered   = (int) progressList.stream()
                .filter(p -> p.getProgressPercentage() >= 100.0).count();
        int notStarted = (int) progressList.stream()
                .filter(p -> p.getProgressPercentage() == 0.0).count();
        int inProgress = progressList.size() - mastered - notStarted;

        // ── Scores ────────────────────────────────────────────────────────────
        double totalScore = progressList.stream()
                .mapToDouble(UserSkillProgress::getScore).sum();

        double avgProgress = progressList.isEmpty() ? 0.0 :
                Math.round(progressList.stream()
                        .mapToDouble(UserSkillProgress::getProgressPercentage)
                        .average().orElse(0.0) * 10.0) / 10.0;

        // ── Top skill ─────────────────────────────────────────────────────────
        UserSkillProgress topSkill = progressList.stream()
                .max(Comparator.comparingDouble(
                        UserSkillProgress::getProgressPercentage))
                .orElse(null);

        // ── Activity stats ────────────────────────────────────────────────────
        long totalActivities = activityLogRepository.countByUser(user);

        // FIX: sumTimeSpentByUser now returns Long — was int causing type mismatch.
        Long totalMinutes = activityLogRepository.sumTimeSpentByUser(user);

        // ── Per-skill breakdown (highest progress first) ───────────────────────
        List<DashboardResponse.SkillSummary> breakdown = progressList.stream()
                .map(p -> DashboardResponse.SkillSummary.builder()
                        .skillId(p.getSkill().getId())
                        .skillName(p.getSkill().getName())
                        .progressPercentage(p.getProgressPercentage())
                        .score(p.getScore())
                        .lastPracticedAt(p.getLastPracticedAt() != null
                                ? p.getLastPracticedAt().toString() : "Never")
                        .build())
                .sorted(Comparator.comparingDouble(
                        DashboardResponse.SkillSummary::getProgressPercentage)
                        .reversed())
                .toList();

        return DashboardResponse.builder()
                .userName(user.getName())
                .totalSkillsTracked(progressList.size())
                .skillsMastered(mastered)
                .skillsInProgress(inProgress)
                .skillsNotStarted(notStarted)
                .totalScore(totalScore)
                .averageProgress(avgProgress)
                .totalActivitiesLogged((int) totalActivities)
                .totalMinutesPracticed(totalMinutes != null ? totalMinutes : 0L)
                .topSkillName(topSkill != null ? topSkill.getSkill().getName() : "N/A")
                .topSkillProgress(topSkill != null ? topSkill.getProgressPercentage() : 0.0)
                .skillBreakdown(breakdown)
                .build();
    }
}