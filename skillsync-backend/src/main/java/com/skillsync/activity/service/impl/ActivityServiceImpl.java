package com.skillsync.activity.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillsync.activity.entity.ActivityLog;
import com.skillsync.activity.repository.ActivityLogRepository;
import com.skillsync.activity.service.ActivityService;
import com.skillsync.common.constant.ActivityType;
import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.skill.entity.Skill;
import com.skillsync.skill.progress.entity.UserSkillProgress;
import com.skillsync.skill.progress.repository.UserSkillProgressRepository;
import com.skillsync.skill.repository.SkillRepository;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private static final Logger log =
            LoggerFactory.getLogger(ActivityServiceImpl.class);

    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserSkillProgressRepository progressRepository;

    @Override
    @Transactional
    public void logActivity(Long skillId, Integer timeSpent) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found", "USER_NOT_FOUND"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Skill not found", "SKILL_NOT_FOUND"));

        // 1. Save the activity log
        ActivityLog activity = ActivityLog.builder()
                .user(user)
                .skill(skill)
                .activityType(ActivityType.PRACTICE)
                .timeSpentMinutes(timeSpent)
                .build();

        activityLogRepository.save(activity);
        log.info("Activity logged for user: {} | skill: {} | time: {} min",
                email, skill.getName(), timeSpent);

        // 2. Find or create progress record for this user+skill combination
        //    FIX: was incorrectly casting List to UserSkillProgress
        UserSkillProgress progress = progressRepository
                .findByUserAndSkill(user, skill)
                .orElseGet(() -> {
                    log.info("No progress record found for user: {} and skill: {}. Creating new one.",
                            email, skill.getName());
                    UserSkillProgress newProgress = new UserSkillProgress();
                    newProgress.setUser(user);
                    newProgress.setSkill(skill);
                    newProgress.setScore(0.0);
                    newProgress.setProgressPercentage(0.0);
                    return newProgress;
                });

        // 3. Update score and progress
        //    Score increases by 0.1 per minute practiced
        //    Progress capped at 100%
        double newScore = progress.getScore() + (timeSpent * 0.1);
        double newProgress = Math.min(100.0,
                progress.getProgressPercentage() + (timeSpent * 0.05));

        progress.setScore(newScore);
        progress.setProgressPercentage(newProgress);
        progress.setLastPracticedAt(LocalDateTime.now());

        progressRepository.save(progress);

        log.info("Progress updated for user: {} | skill: {} | score: {} | progress: {}%",
                email, skill.getName(), newScore, newProgress);
    }
}
