package com.skillsync.dsa.problem.entity;

import java.util.HashSet;
import java.util.Set;

import com.skillsync.common.util.BaseEntity;
import com.skillsync.dsa.common.Difficulty;
import com.skillsync.dsa.tag.entity.Tag;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "dsa_problems",
        uniqueConstraints = @UniqueConstraint(columnNames = {"slug"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem extends BaseEntity {

    @Column(nullable = false)
    private String title;

    /**
     * Stable unique identifier used in URLs. Example: "two-sum".
     */
    @Column(nullable = false)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(length = 2000)
    private String description;

    @Column
    private String sourceUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "dsa_problem_tags",
            joinColumns = @JoinColumn(name = "problem_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
}

