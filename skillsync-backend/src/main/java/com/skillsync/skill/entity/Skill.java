package com.skillsync.skill.entity;

import com.skillsync.activity.entity.ActivityLog;
import com.skillsync.common.constant.Difficulty;
import com.skillsync.common.util.BaseEntity;
import com.skillsync.roadmap.entity.RoadmapStep;
import com.skillsync.skill.category.entity.SkillCategory;
import com.skillsync.skill.progress.entity.UserSkillProgress;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private SkillCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(length = 500)
    private String description;

    // FIX: cascade + orphanRemoval added so that if an admin deletes a Skill,
    // all associated UserSkillProgress, ActivityLog, and RoadmapStep rows are
    // also deleted, preventing FK constraint violations.
    @OneToMany(mappedBy = "skill",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserSkillProgress> progressList = new ArrayList<>();

    @OneToMany(mappedBy = "skill",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @Builder.Default
    private List<ActivityLog> activityLogs = new ArrayList<>();

    @OneToMany(mappedBy = "skill",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @Builder.Default
    private List<RoadmapStep> roadmapSteps = new ArrayList<>();
}