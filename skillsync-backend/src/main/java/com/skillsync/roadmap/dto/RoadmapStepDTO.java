package com.skillsync.roadmap.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoadmapStepDTO {

    private Long    stepId;
    private Integer stepOrder;
    private String  skillName;
    private Long    skillId;
    private String  title;
    private String  description;
    private Integer estimatedDays;
    private Boolean completed;
}