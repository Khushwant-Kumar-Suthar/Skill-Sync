package com.skillsync.skill.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skillsync.skill.category.entity.SkillCategory;

public interface SkillCategoryRepository
        extends JpaRepository<SkillCategory, Long> {

    // FIX: replaces findAll() + lazy skills access which caused N+1 queries.
    // A single JOIN FETCH loads categories and their skills in one SQL statement.
    @Query("SELECT DISTINCT c FROM SkillCategory c LEFT JOIN FETCH c.skills")
    List<SkillCategory> findAllWithSkills();
    
}