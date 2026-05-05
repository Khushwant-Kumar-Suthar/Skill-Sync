package com.skillsync.dsa.problem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillsync.dsa.common.Difficulty;
import com.skillsync.dsa.problem.entity.Problem;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Problem> findByDifficultyOrderByTitleAsc(Difficulty difficulty);

    List<Problem> findDistinctByTags_NameIgnoreCaseOrderByTitleAsc(String tagName);

    List<Problem> findDistinctByDifficultyAndTags_NameIgnoreCaseOrderByTitleAsc(
            Difficulty difficulty,
            String tagName
    );
}

