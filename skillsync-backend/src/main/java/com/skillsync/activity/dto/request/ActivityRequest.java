package com.skillsync.activity.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityRequest {

    @NotNull(message = "Skill ID is required")
    private Long skillId;

    @NotNull(message = "Time spent is required")
    // FIX: @Positive was correct but @Min(1) is more explicit.
    // @Max(1440) added — 1440 minutes = 24 hours — prevents a user logging
    // e.g. 999999 minutes which would set progress to 100% in one call
    // and inflate scores unrealistically.
    @Min(value = 1, message = "Time spent must be at least 1 minute")
    @Max(value = 1440, message = "Time spent cannot exceed 1440 minutes (24 hours) per session")
    private Integer timeSpentMinutes;
}