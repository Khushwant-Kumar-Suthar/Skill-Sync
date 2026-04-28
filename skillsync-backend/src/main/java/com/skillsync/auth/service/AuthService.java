package com.skillsync.auth.service;

import com.skillsync.auth.dto.AuthResponseDto;
import com.skillsync.auth.dto.LoginRequest;
import com.skillsync.auth.dto.RegisterRequest;

public interface AuthService {
	
    String register(RegisterRequest request);
    
    AuthResponseDto login(LoginRequest request);
}