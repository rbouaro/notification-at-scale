package com.rbouaro.notificationatscale.user.model.dto;

import java.util.UUID;

public record UserCredentials(Long id, UUID uuid, String username, String passwordHash) {}
