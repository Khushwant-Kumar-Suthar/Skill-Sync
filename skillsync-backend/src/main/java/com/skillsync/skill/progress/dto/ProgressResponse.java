package com.skillsync.skill.progress.dto;


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
public class ProgressResponse {

    private Long skillId;
    private String skillName;

    private Double progressPercentage;
    private Double score;

    private String lastPracticedAt;
}
