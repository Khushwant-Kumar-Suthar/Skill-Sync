package com.skillsync.roadmap.entity;

import com.skillsync.common.util.BaseEntity;
import com.skillsync.skill.entity.Skill;
import com.skillsync.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
