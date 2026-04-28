package com.skillsync.skill.category.dto;

import lombok.*;

import java.util.List;

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