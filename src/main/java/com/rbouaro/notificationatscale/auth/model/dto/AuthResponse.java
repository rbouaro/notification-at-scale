package com.rbouaro.notificationatscale.auth.model.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {}
