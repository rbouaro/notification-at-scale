package com.rbouaro.notificationatscale.user.model.dto;

import jakarta.validation.constraints.NotNull;

public record PreferencePatch(@NotNull Boolean enabled) {}
