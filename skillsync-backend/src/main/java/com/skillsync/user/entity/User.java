package com.skillsync.user.entity;

import com.skillsync.activity.entity.ActivityLog;
import com.skillsync.common.constant.Role;
import com.skillsync.common.util.BaseEntity;
import com.skillsync.roadmap.entity.RoadmapStep;
import com.skillsync.skill.progress.entity.UserSkillProgress;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // FIX: cascade + orphanRemoval so that when a User is deleted,
    // all child records are also deleted and no FK violations occur.
    @OneToMany(mappedBy = "user",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserSkillProgress> skillProgressList = new ArrayList<>();

    @OneToMany(mappedBy = "user",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @Builder.Default
    private List<ActivityLog> activityLogs = new ArrayList<>();

    @OneToMany(mappedBy = "user",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @Builder.Default
    private List<RoadmapStep> roadmapSteps = new ArrayList<>();
}