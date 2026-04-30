package com.skillsync.dsa.attempt.service;

import com.skillsync.dsa.attempt.dto.AttemptDTO;
import com.skillsync.dsa.attempt.dto.CreateAttemptRequest;

import java.util.List;

public interface AttemptService {
    AttemptDTO createAttempt(Long problemId, CreateAttemptRequest request);
    List<AttemptDTO> listMyAttempts();
}

