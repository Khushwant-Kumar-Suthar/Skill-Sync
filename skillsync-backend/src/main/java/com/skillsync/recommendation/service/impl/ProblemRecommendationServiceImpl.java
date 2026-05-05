package com.skillsync.recommendation.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.dsa.attempt.entity.Attempt;
import com.skillsync.dsa.attempt.repository.AttemptRepository;
import com.skillsync.dsa.common.AttemptStatus;
import com.skillsync.dsa.problem.entity.Problem;
import com.skillsync.dsa.problem.repository.ProblemRepository;
import com.skillsync.dsa.tag.entity.Tag;
import com.skillsync.recommendation.dto.ProblemRecommendationDTO;
import com.skillsync.recommendation.service.ProblemRecommendationService;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemRecommendationServiceImpl implements ProblemRecommendationService {

    private final UserRepository userRepository;
    private final AttemptRepository attemptRepository;
    private final ProblemRepository problemRepository;

    /**
     * MVP: rule-based recommendations.
     * - Find tags where user has ATTEMPTED (not SOLVED) the most.
     * - Recommend EASY/MEDIUM problems from those tags (excluding already SOLVED).
     */
    @Override
    public List<ProblemRecommendationDTO> recommendProblems() {
        User user = getCurrentUser();

        List<Attempt> attempted = attemptRepository
                .findByUserAndStatusOrderByAttemptedAtDesc(user, AttemptStatus.ATTEMPTED);
        List<Attempt> solved = attemptRepository
                .findByUserAndStatusOrderByAttemptedAtDesc(user, AttemptStatus.SOLVED);

        Set<Long> solvedProblemIds = solved.stream()
                .map(a -> a.getProblem().getId())
                .collect(java.util.stream.Collectors.toSet());

        Map<String, Integer> tagWeakness = new HashMap<>();
        for (Attempt a : attempted) {
            for (Tag t : a.getProblem().getTags()) {
                String key = t.getType().name() + ":" + t.getName().toLowerCase();
                tagWeakness.merge(key, 1, Integer::sum);
            }
        }

        List<String> topWeakTags = tagWeakness.entrySet()
                .stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        if (topWeakTags.isEmpty()) {
            // Cold-start: just recommend some easy problems (if any exist)
            return problemRepository.findByDifficultyOrderByTitleAsc(
                            com.skillsync.dsa.common.Difficulty.EASY)
                    .stream()
                    .limit(10)
                    .map(p -> toDTO(p, "Start with a few easy problems to build momentum."))
                    .toList();
        }

        List<ProblemRecommendationDTO> out = new ArrayList<>();
        Set<Long> added = new HashSet<>();

        for (String weakTag : topWeakTags) {
            String[] parts = weakTag.split(":", 2);
            String tagName = parts.length == 2 ? parts[1] : weakTag;

            List<Problem> candidates = problemRepository
                    .findDistinctByTags_NameIgnoreCaseOrderByTitleAsc(tagName);

            for (Problem p : candidates) {
                if (out.size() >= 10) break;
                if (solvedProblemIds.contains(p.getId())) continue;
                if (!added.add(p.getId())) continue;
                out.add(toDTO(p, "Recommended because you struggled with tag: " + weakTag));
            }
            if (out.size() >= 10) break;
        }

        return out;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found", "USER_NOT_FOUND"));
    }

    private ProblemRecommendationDTO toDTO(Problem p, String reason) {
        Set<String> tags = p.getTags()
                .stream()
                .map(t -> t.getType().name() + ":" + t.getName())
                .collect(java.util.stream.Collectors.toSet());

        return ProblemRecommendationDTO.builder()
                .problemId(p.getId())
                .title(p.getTitle())
                .slug(p.getSlug())
                .difficulty(p.getDifficulty())
                .reason(reason)
                .tags(tags)
                .build();
    }
}

