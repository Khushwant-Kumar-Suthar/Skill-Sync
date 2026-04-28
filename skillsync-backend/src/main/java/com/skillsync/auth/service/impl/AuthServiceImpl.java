package com.skillsync.auth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skillsync.auth.dto.AuthResponseDto;
import com.skillsync.auth.dto.LoginRequest;
import com.skillsync.auth.dto.RegisterRequest;
import com.skillsync.auth.service.AuthService;
import com.skillsync.common.constant.Role;
import com.skillsync.common.exception.BadRequestException;
import com.skillsync.common.security.JwtUtil;
import com.skillsync.user.entity.User;
import com.skillsync.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger log =
            LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Email already exists", "EMAIL_ALREADY_EXISTS");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        log.info("New user registered: {}", request.getEmail());

        return "User registered successfully";
    }

    @Override
    public AuthResponseDto login(LoginRequest request) {

        log.info("Login attempt for email: {}", request.getEmail());

        try {
            // authenticationManager handles both "user not found"
            // and "wrong password" — no need to check again manually
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for: {}", request.getEmail());
            throw new BadRequestException(
                    "Invalid email or password", "INVALID_CREDENTIALS");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException(
                        "User not found", "USER_NOT_FOUND"));

        String token = jwtUtil.generateToken(user.getEmail());

        log.info("Authentication successful for: {}", request.getEmail());

        return AuthResponseDto.builder()
                .token(token)
                .type("Bearer")
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
