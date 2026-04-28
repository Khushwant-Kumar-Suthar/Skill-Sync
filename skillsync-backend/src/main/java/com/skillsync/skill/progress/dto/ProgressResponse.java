package com.skillsync.skill.progress.dto;


import lombok.*;

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
