package com.rbouaro.notificationatscale.auth.api;

import com.rbouaro.notificationatscale.auth.model.dto.AuthResponse;
import com.rbouaro.notificationatscale.auth.model.dto.LoginRequest;
import com.rbouaro.notificationatscale.auth.model.dto.RegisterRequest;
import com.rbouaro.notificationatscale.auth.model.dto.TokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Authentication", description = "Registration and login endpoints")
@RequestMapping("/auth")
public interface AuthApi {

    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "409", description = "Username or email already in use")
    @PostMapping("/register")
    ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request);

    @Operation(summary = "Login with email and password")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request);

    @Operation(summary = "Refresh access token")
    @ApiResponse(responseCode = "200", description = "New access and refresh tokens issued")
    @ApiResponse(responseCode = "401", description = "Refresh token is invalid, expired, or revoked")
    @PostMapping("/refresh")
    ResponseEntity<AuthResponse> refresh(@Valid @RequestBody TokenRequest request);

    @Operation(summary = "Logout and revoke refresh token")
    @ApiResponse(responseCode = "204", description = "Refresh token revoked")
    @ApiResponse(responseCode = "401", description = "Refresh token is invalid, expired, or revoked")
    @PostMapping("/logout")
    ResponseEntity<Void> logout(@Valid @RequestBody TokenRequest request);
}
