package com.skillsync.common.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private boolean success;
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
}