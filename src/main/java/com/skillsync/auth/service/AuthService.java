package com.skillsync.auth.service;

import com.skillsync.auth.dto.request.RegisterRequestDto;
import com.skillsync.auth.dto.response.UserResponseDto;

public interface AuthService {
     UserResponseDto register(RegisterRequestDto request);
}
