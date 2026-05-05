package com.skillsync.dsa.problem.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.skillsync.common.exception.BadRequestException;
import com.skillsync.common.exception.ResourceNotFoundException;
import com.skillsync.dsa.common.Difficulty;
import com.skillsync.dsa.common.TagType;
import com.skillsync.dsa.problem.dto.CreateProblemRequest;
import com.skillsync.dsa.problem.dto.ProblemDTO;
import com.skillsync.dsa.problem.entity.Problem;
import com.skillsync.dsa.problem.repository.ProblemRepository;
import com.skillsync.dsa.problem.service.ProblemService;
import com.skillsync.dsa.tag.entity.Tag;
import com.skillsync.dsa.tag.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public ProblemDTO getProblemBySlug(String slug) {
        Problem problem = problemRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Problem not found", "PROBLEM_NOT_FOUND"));
        return toDTO(problem);
    }

    @Override
    @Transactional
    public ProblemDTO createProblem(CreateProblemRequest request) {
        String slug = request.getSlug() == null || request.getSlug().isBlank()
                ? slugify(request.getTitle())
                : slugify(request.getSlug());

        if (problemRepository.existsBySlug(slug)) {
            throw new BadRequestException(
                    "A problem with this slug already exists",
                    "PROBLEM_SLUG_EXISTS");
        }

        Set<Tag> tags = request.getTags() == null ? Set.of()
                : request.getTags().stream()
                        .filter(raw -> raw != null && !raw.isBlank())
                        .map(this::resolveTag)
                        .collect(Collectors.toSet());

        Problem problem = Problem.builder()
                .title(request.getTitle().trim())
                .slug(slug)
                .difficulty(request.getDifficulty())
                .description(request.getDescription())
                .sourceUrl(request.getSourceUrl())
                .tags(tags)
                .build();

        return toDTO(problemRepository.save(problem));
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
                .description(p.getDescription())
                .sourceUrl(p.getSourceUrl())
                .tags(tags)
                .build();
    }

    private Tag resolveTag(String rawTag) {
        String[] parts = rawTag.split(":", 2);
        TagType type = TagType.TOPIC;
        String name = rawTag.trim();

        if (parts.length == 2) {
            try {
                type = TagType.valueOf(parts[0].trim().toUpperCase());
                name = parts[1].trim();
            } catch (IllegalArgumentException ignored) {
                name = rawTag.trim();
            }
        }

        if (name.isBlank()) {
            throw new BadRequestException("Tag name cannot be blank", "INVALID_TAG");
        }

        TagType finalType = type;
        String finalName = name;
        return tagRepository.findByTypeAndNameIgnoreCase(finalType, finalName)
                .orElseGet(() -> tagRepository.save(Tag.builder()
                        .type(finalType)
                        .name(finalName)
                        .build()));
    }

    private String slugify(String value) {
        String slug = value == null ? "" : value.trim().toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        if (slug.isBlank()) {
            throw new BadRequestException("Problem title must create a valid slug", "INVALID_SLUG");
        }

        return slug;
    }
}

