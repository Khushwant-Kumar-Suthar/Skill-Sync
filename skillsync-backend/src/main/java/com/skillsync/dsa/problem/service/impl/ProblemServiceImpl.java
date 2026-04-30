package com.skillsync.dsa.problem.service.impl;

import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.dsa.common.Difficulty;
import com.skillsync.dsa.problem.dto.ProblemDTO;
import com.skillsync.dsa.problem.entity.Problem;
import com.skillsync.dsa.problem.repository.ProblemRepository;
import com.skillsync.dsa.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;

    @Override
    public List<ProblemDTO> listProblems(Difficulty difficulty, String tag) {
        List<Problem> problems;

        if (difficulty != null && tag != null && !tag.isBlank()) {
            problems = problemRepository
                    .findDistinctByDifficultyAndTags_NameIgnoreCaseOrderByTitleAsc(
                            difficulty, tag);
        } else if (tag != null && !tag.isBlank()) {
            problems = problemRepository
                    .findDistinctByTags_NameIgnoreCaseOrderByTitleAsc(tag);
        } else if (difficulty != null) {
            problems = problemRepository
                    .findByDifficultyOrderByTitleAsc(difficulty);
        } else {
            problems = problemRepository.findAll()
                    .stream()
                    .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
                    .toList();
        }

        return problems.stream().map(this::toDTO).toList();
    }

    @Override
    public ProblemDTO getProblemBySlug(String slug) {
        Problem problem = problemRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Problem not found", "PROBLEM_NOT_FOUND"));
        return toDTO(problem);
    }

    private ProblemDTO toDTO(Problem p) {
        Set<String> tags = p.getTags()
                .stream()
                .map(t -> t.getType().name() + ":" + t.getName())
                .collect(java.util.stream.Collectors.toSet());

        return ProblemDTO.builder()
                .id(p.getId())
                .title(p.getTitle())
                .slug(p.getSlug())
                .difficulty(p.getDifficulty())
                .sourceUrl(p.getSourceUrl())
                .tags(tags)
                .build();
    }
}

