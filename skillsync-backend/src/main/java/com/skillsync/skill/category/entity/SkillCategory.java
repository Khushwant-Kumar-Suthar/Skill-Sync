package com.skillsync.skill.category.entity;

import java.util.ArrayList;
import java.util.List;

import com.skillsync.common.util.BaseEntity;
import com.skillsync.skill.entity.Skill;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "skill_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillCategory extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    // FIX 1: cascade + orphanRemoval added. Without this, deleting a category
    //         whose skills have child records (progress, activity) crashes with FK violation.
    // FIX 2: @Builder.Default added. Without it, Lombok builder sets skills = null,
    //         causing NullPointerException when category.getSkills() is called
    //         on a newly built (not-yet-persisted) SkillCategory instance.
    @OneToMany(mappedBy = "category",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @Builder.Default
    private List<Skill> skills = new ArrayList<>();
}