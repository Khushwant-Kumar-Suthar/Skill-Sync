package com.skillsync.dsa.attempt.dto;

import com.skillsync.dsa.common.AttemptStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAttemptRequest {

    @NotNull
    private AttemptStatus status;

    private String language;

    private String notes;
}

