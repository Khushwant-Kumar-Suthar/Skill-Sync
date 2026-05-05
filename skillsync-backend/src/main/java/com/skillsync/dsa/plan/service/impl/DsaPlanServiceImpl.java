package com.skillsync.dsa.plan.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.dsa.attempt.repository.AttemptRepository;
import com.skillsync.dsa.common.AttemptStatus;
import com.skillsync.dsa.plan.dto.DsaPlanDTO;
import com.skillsync.dsa.plan.dto.PlanProgressDTO;
import com.skillsync.dsa.plan.entity.DsaPlan;
import com.skillsync.dsa.plan.repository.DsaPlanItemRepository;
import com.skillsync.dsa.plan.repository.DsaPlanRepository;
import com.skillsync.dsa.plan.service.DsaPlanService;
import com.skillsync.dsa.problem.dto.ProblemDTO;
import com.skillsync.dsa.problem.entity.Problem;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DsaPlanServiceImpl implements DsaPlanService {

    private final DsaPlanRepository planRepository;
    private final DsaPlanItemRepository planItemRepository;
    private final AttemptRepository attemptRepository;
    private final UserRepository userRepository;

    @Override
    public List<DsaPlanDTO> listPlans() {
        List<DsaPlan> plans = planRepository.findAll()
                .stream()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .toList();

        return plans.stream().map(p -> DsaPlanDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .slug(p.getSlug())
                .description(p.getDescription())
                .totalProblems(null) // keep lightweight; frontend can call /problems for count if needed
                .build()).toList();
    }

    @Override
    public List<ProblemDTO> listPlanProblems(String planSlug) {
        DsaPlan plan = planRepository.findBySlug(planSlug)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Plan not found", "PLAN_NOT_FOUND"));

        return planItemRepository.findItemsWithProblemAndTagsByPlan(plan)
                .stream()
                .map(pi -> toDTO(pi.getProblem()))
                .toList();
    }

    @Override
    public PlanProgressDTO getMyPlanProgress(String planSlug) {
        DsaPlan plan = planRepository.findBySlug(planSlug)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Plan not found", "PLAN_NOT_FOUND"));

        User user = getCurrentUser();

        long total = planItemRepository.countByPlan(plan);
        long solved = attemptRepository.countDistinctProblemsInPlanByUserAndStatus(
                plan, user, AttemptStatus.SOLVED);
        long attempted = attemptRepository.countDistinctProblemsInPlanByUserAndStatus(
                plan, user, AttemptStatus.ATTEMPTED);
        long remaining = Math.max(0, total - solved);
        double completion = total == 0 ? 0.0 : (solved * 100.0) / total;

        return PlanProgressDTO.builder()
                .planSlug(plan.getSlug())
                .planName(plan.getName())
                .totalProblems(total)
                .solved(solved)
                .attempted(attempted)
                .remaining(remaining)
                .completionPercent(Math.round(completion * 10.0) / 10.0)
                .build();
    }

    private ProblemDTO toDTO(Problem p) {
        Set<String> tags = p.getTags()
                .stream()
                .map(t -> t.getType().name() + ":" + t.getName())
                .collect(Collectors.toSet());

        return ProblemDTO.builder()
                .id(p.getId())
                .title(p.getTitle())
                .slug(p.getSlug())
                .difficulty(p.getDifficulty())
                .sourceUrl(p.getSourceUrl())
                .tags(tags)
                .build();
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
}

