package com.skillsync.skill.progress.entity;

import java.time.LocalDateTime;

import com.skillsync.common.util.BaseEntity;
import com.skillsync.skill.entity.Skill;
import com.skillsync.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "user_skill_progress",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "skill_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkillProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    // FIX: @Builder ignores field initializers (= 0.0) unless @Builder.Default is used.
    // Without this, Lombok's builder would set these to null, causing a NPE
    // when Hibernate tries to persist @Column(nullable = false) Double fields.
    @Column(nullable = false)
    @Builder.Default
    private Double progressPercentage = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Double score = 0.0;

    // Set manually in ActivityServiceImpl — no auto-timestamp annotation needed.
    @Column
    private LocalDateTime lastPracticedAt;
}

