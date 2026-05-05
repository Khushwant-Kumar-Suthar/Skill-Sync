package com.skillsync.skill.progress.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skillsync.skill.entity.Skill;
import com.skillsync.skill.progress.entity.UserSkillProgress;
import com.skillsync.user.entity.User;

public interface UserSkillProgressRepository
        extends JpaRepository<UserSkillProgress, Long> {

    // All progress records for a user (skill eagerly loaded)
    @Query("SELECT p FROM UserSkillProgress p JOIN FETCH p.skill WHERE p.user = :user")
    List<UserSkillProgress> findByUserWithSkill(@Param("user") User user);

    @Query("SELECT p FROM UserSkillProgress p " +
           "JOIN FETCH p.user " +
           "JOIN FETCH p.skill s " +
           "JOIN FETCH s.category " +
           "ORDER BY p.user.name ASC, s.category.name ASC, s.name ASC")
    List<UserSkillProgress> findAllForAdmin();

    // Specific skill progress for a user
    @Query("SELECT p FROM UserSkillProgress p JOIN FETCH p.skill " +
           "WHERE p.user = :user AND p.skill = :skill")
    Optional<UserSkillProgress> findByUserAndSkill(
            @Param("user") User user,
            @Param("skill") Skill skill);

    // Count of skills being tracked by a user
    long countByUser(User user);

    // Average progress percentage for a specific user
    @Query("SELECT COALESCE(AVG(p.progressPercentage), 0) FROM UserSkillProgress p WHERE p.user = :user")
    double avgProgressByUser(@Param("user") User user);

    // Platform-wide average progress (admin stats)
    @Query("SELECT COALESCE(AVG(p.progressPercentage), 0) FROM UserSkillProgress p")
    double platformAverageProgress();
}
