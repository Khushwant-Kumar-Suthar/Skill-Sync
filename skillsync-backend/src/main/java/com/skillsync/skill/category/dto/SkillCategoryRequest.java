package com.skillsync.skill.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillCategoryRequest {

    @NotBlank
    private String name;

    private String description;
}