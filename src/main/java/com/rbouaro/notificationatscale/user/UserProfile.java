package com.rbouaro.notificationatscale.user;

import java.time.Instant;
import java.util.UUID;

public record UserProfile(
        UUID id,
        String username,
        String email,
        String displayName,
        String bio,
        String avatarUrl,
        Instant createdAt,
        Instant updatedAt
) {}
