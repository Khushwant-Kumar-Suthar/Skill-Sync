package com.skillsync.dsa.attempt.dto;

import com.skillsync.dsa.common.AttemptStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttemptDTO {
    private Long id;
    private Long problemId;
    private String problemTitle;
    private AttemptStatus status;
    private String language;
    private String notes;
    private String attemptedAt;
}

