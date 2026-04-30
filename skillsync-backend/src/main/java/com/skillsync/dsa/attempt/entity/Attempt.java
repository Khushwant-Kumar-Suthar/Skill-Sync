package com.skillsync.dsa.attempt.entity;

import com.skillsync.common.util.BaseEntity;
import com.skillsync.dsa.common.AttemptStatus;
import com.skillsync.dsa.problem.entity.Problem;
import com.skillsync.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dsa_attempts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attempt extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttemptStatus status;

    @Column
    private String language;

    @Column(length = 4000)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime attemptedAt;
}

