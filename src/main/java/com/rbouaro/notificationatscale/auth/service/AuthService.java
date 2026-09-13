package com.rbouaro.notificationatscale.auth.service;

import com.rbouaro.notificationatscale.auth.model.dto.AuthResponse;
import com.rbouaro.notificationatscale.auth.model.dto.LoginRequest;
import com.rbouaro.notificationatscale.auth.model.dto.RegisterRequest;
import com.rbouaro.notificationatscale.auth.model.entity.RefreshToken;
import com.rbouaro.notificationatscale.auth.model.repository.RefreshTokenRepository;
import com.rbouaro.notificationatscale.config.JwtProperties;
import com.rbouaro.notificationatscale.exception.ConflictException;
import com.rbouaro.notificationatscale.user.model.dto.UserCredentials;
import com.rbouaro.notificationatscale.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new ConflictException("email", request.getEmail());
        }
        if (userService.existsByUsername(request.getUsername())) {
            throw new ConflictException("username", request.getUsername());
        }
        String passwordHash = passwordEncoder.encode(request.getPassword());
        UserCredentials credentials = userService.register(request.getUsername(), request.getEmail(), passwordHash);
        return buildAuthResponse(credentials);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        UserCredentials credentials = userService.findCredentialsByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), credentials.passwordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        refreshTokenRepository.revokeAllByUserId(credentials.id());
        return buildAuthResponse(credentials);
    }

    private AuthResponse buildAuthResponse(UserCredentials credentials) {
        String accessToken = jwtService.generateAccessToken(
                credentials.id(), credentials.uuid(), credentials.username());
        String refreshTokenValue = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(jwtProperties.refreshTokenExpiry());
        refreshTokenRepository.save(new RefreshToken(refreshTokenValue, credentials.id(), expiresAt));
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .expiresIn(jwtProperties.accessTokenExpiry())
                .build();
    }
}
