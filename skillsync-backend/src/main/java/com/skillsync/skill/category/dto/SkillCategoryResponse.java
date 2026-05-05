package com.skillsync.skill.category.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillCategoryResponse {

    private Long categoryId;
    private String categoryName;

    private List<SkillDTO> skills;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SkillDTO {
        private Long id;
        private String name;
    }
}