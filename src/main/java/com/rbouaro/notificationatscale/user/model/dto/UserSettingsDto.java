package com.rbouaro.notificationatscale.user.model.dto;

import com.rbouaro.notificationatscale.user.model.entity.TwoFaMethod;

public record UserSettingsDto(boolean twoFaEnabled, TwoFaMethod twoFaMethod, String language) {}
