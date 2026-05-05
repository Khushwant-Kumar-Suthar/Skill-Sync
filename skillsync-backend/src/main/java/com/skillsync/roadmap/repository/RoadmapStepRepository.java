package com.skillsync.roadmap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skillsync.roadmap.entity.RoadmapStep;
import com.skillsync.user.entity.User;

public interface RoadmapStepRepository
        extends JpaRepository<RoadmapStep, Long> {

    // Fetch all steps for a user with skill eagerly loaded (avoids N+1)
    @Query("SELECT r FROM RoadmapStep r JOIN FETCH r.skill " +
           "WHERE r.user = :user ORDER BY r.stepOrder ASC")
    List<RoadmapStep> findByUserOrderByStepOrder(@Param("user") User user);

    @Query("SELECT r FROM RoadmapStep r JOIN FETCH r.user JOIN FETCH r.skill " +
           "ORDER BY r.user.name ASC, r.stepOrder ASC")
    List<RoadmapStep> findAllForAdmin();

    // Used to detect if a specific skill already has a step in the roadmap
    boolean existsByUserAndSkillId(User user, Long skillId);

    // Bulk delete all roadmap steps for a user (used on regeneration)
    @Modifying
    @Query("DELETE FROM RoadmapStep r WHERE r.user = :user")
    void deleteAllByUser(@Param("user") User user);
}
