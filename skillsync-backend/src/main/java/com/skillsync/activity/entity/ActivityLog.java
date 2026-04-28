package com.skillsync.activity.entity;

import com.skillsync.common.constant.ActivityType;
import com.skillsync.common.util.BaseEntity;
import com.skillsync.skill.entity.Skill;
import com.skillsync.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "activity_logs")
@Getter
@Setter
@NoArgsConstructor   // FIX: required by JPA — was missing, causing proxy issues
@AllArgsConstructor
@Builder
public class ActivityLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType activityType;

    @Column(nullable = false)
    private Integer timeSpentMinutes;

    @Column(length = 500)
    private String notes;
}