package com.skillsync.skill.progress.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.skill.progress.dto.ProgressResponse;
import com.skillsync.skill.progress.entity.UserSkillProgress;
import com.skillsync.skill.progress.repository.UserSkillProgressRepository;
import com.skillsync.skill.progress.service.ProgressService;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private static final Logger logger =
            LoggerFactory.getLogger(ProgressServiceImpl.class);

    private final UserSkillProgressRepository progressRepository;
    private final UserRepository              userRepository;

    @Override
    public List<ProgressResponse> getUserProgress() {

        String email = SecurityContextHolder
                .getContext().getAuthentication().getName();

        logger.info("Fetching progress for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found", "USER_NOT_FOUND"));

        List<UserSkillProgress> progressList =
                progressRepository.findByUserWithSkill(user);

        return progressList.stream()
                .map(p -> ProgressResponse.builder()
                        .skillId(p.getSkill().getId())
                        .skillName(p.getSkill().getName())
                        .progressPercentage(p.getProgressPercentage())
                        .score(p.getScore())
                        // FIX: ternary kept but extracted to a method for clarity
                        .lastPracticedAt(formatDateTime(p))
                        .build())
                .toList();
    }

    private String formatDateTime(UserSkillProgress p) {
        return p.getLastPracticedAt() != null
                ? p.getLastPracticedAt().toString()
                : null;
    }
}