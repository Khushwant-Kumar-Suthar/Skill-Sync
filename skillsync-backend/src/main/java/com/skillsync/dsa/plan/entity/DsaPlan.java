package com.skillsync.dsa.plan.entity;

import com.skillsync.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "dsa_plans",
        uniqueConstraints = @UniqueConstraint(columnNames = {"slug"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DsaPlan extends BaseEntity {

    @Column(nullable = false)
    private String name;

    /**
     * Stable unique identifier used in URLs. Example: "top-75".
     */
    @Column(nullable = false)
    private String slug;

    @Column(length = 2000)
    private String description;
}

