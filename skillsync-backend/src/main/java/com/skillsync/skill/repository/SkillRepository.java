package com.skillsync.skill.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.skillsync.skill.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    @EntityGraph(attributePaths = "category")
    Page<Skill> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "category")
    Page<Skill> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}