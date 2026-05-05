package com.skillsync.dsa.attempt.service;

import java.util.List;

import com.skillsync.dsa.attempt.dto.AttemptDTO;
import com.skillsync.dsa.attempt.dto.CreateAttemptRequest;

public interface AttemptService {
    AttemptDTO createAttempt(Long problemId, CreateAttemptRequest request);
    List<AttemptDTO> listMyAttempts();
}

