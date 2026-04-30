package com.skillsync.dsa.plan.entity;

import com.skillsync.common.util.BaseEntity;
import com.skillsync.dsa.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "dsa_plan_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"plan_id", "problem_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DsaPlanItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private DsaPlan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(nullable = false)
    private Integer orderIndex;
}

