package com.skillsync.dsa.attempt.service.impl;

import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.dsa.attempt.dto.AttemptDTO;
import com.skillsync.dsa.attempt.dto.CreateAttemptRequest;
import com.skillsync.dsa.attempt.entity.Attempt;
import com.skillsync.dsa.attempt.repository.AttemptRepository;
import com.skillsync.dsa.attempt.service.AttemptService;
import com.skillsync.dsa.problem.entity.Problem;
import com.skillsync.dsa.problem.repository.ProblemRepository;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttemptServiceImpl implements AttemptService {

    private final AttemptRepository attemptRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AttemptDTO createAttempt(Long problemId, CreateAttemptRequest request) {
        User user = getCurrentUser();
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Problem not found", "PROBLEM_NOT_FOUND"));

        Attempt attempt = Attempt.builder()
                .user(user)
                .problem(problem)
                .status(request.getStatus())
                .language(request.getLanguage())
                .notes(request.getNotes())
                .attemptedAt(LocalDateTime.now())
                .build();

        Attempt saved = attemptRepository.save(attempt);
        return toDTO(saved);
    }

    @Override
    public List<AttemptDTO> listMyAttempts() {
        User user = getCurrentUser();
        return attemptRepository.findByUserOrderByAttemptedAtDesc(user)
                .stream()
                .map(this::toDTO)
                .toList();
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

    private AttemptDTO toDTO(Attempt a) {
        return AttemptDTO.builder()
                .id(a.getId())
                .problemId(a.getProblem().getId())
                .problemTitle(a.getProblem().getTitle())
                .status(a.getStatus())
                .language(a.getLanguage())
                .notes(a.getNotes())
                .attemptedAt(a.getAttemptedAt() != null ? a.getAttemptedAt().toString() : null)
                .build();
    }
}

