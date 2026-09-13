package com.rbouaro.notificationatscale.user.model.dto;

import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;

import java.util.Map;

public record UserPreferencesDto(Map<NotificationType, Map<NotificationChannel, Boolean>> preferences) {}
