package com.skillsync.recommendation.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.recommendation.dto.RecommendationDTO;
import com.skillsync.recommendation.engine.RecommendationEngine;
import com.skillsync.recommendation.service.RecommendationService;
import com.skillsync.skill.progress.entity.UserSkillProgress;
import com.skillsync.skill.progress.repository.UserSkillProgressRepository;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger log =
            LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final UserRepository userRepository;
    private final UserSkillProgressRepository progressRepository;
    private final RecommendationEngine recommendationEngine;

    @Override
    public List<RecommendationDTO> getRecommendations() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        log.info("Generating recommendations for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found", "USER_NOT_FOUND"));

        List<UserSkillProgress> progressList =
                progressRepository.findByUserWithSkill(user);

        List<RecommendationDTO> recommendations =
                recommendationEngine.recommend(progressList);

        log.info("Generated {} recommendations for user: {}",
                recommendations.size(), email);

        return recommendations;
    }
}
