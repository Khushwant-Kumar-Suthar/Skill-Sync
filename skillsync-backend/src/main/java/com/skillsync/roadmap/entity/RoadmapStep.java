package com.skillsync.roadmap.entity;

import com.skillsync.common.util.BaseEntity;
import com.skillsync.user.entity.User;
import com.skillsync.skill.entity.Skill;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roadmap_steps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoadmapStep extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private Integer stepOrder;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer estimatedDays;

    // FIX: @Builder.Default so builder sets this to false, not null.
    // Without it, Boolean completed = null would cause a NullPointerException
    // when RoadmapStep::getCompleted is used in stream filters.
    @Column(nullable = false)
    @Builder.Default
    private Boolean completed = false;
}
