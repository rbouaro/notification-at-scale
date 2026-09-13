package com.rbouaro.notificationatscale.user.api;

import com.rbouaro.notificationatscale.user.model.dto.NotificationChannelPreferencesDto;
import com.rbouaro.notificationatscale.user.model.dto.PreferencePatch;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;
import com.rbouaro.notificationatscale.user.service.NotificationChannelPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationChannelPreferenceController implements NotificationChannelPreferenceApi {

    private final NotificationChannelPreferenceService notificationChannelPreferenceService;

    @Override
    public ResponseEntity<NotificationChannelPreferencesDto> getMyPreferences(Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("user_id")).longValue();
        return ResponseEntity.ok(notificationChannelPreferenceService.getByUserId(userId));
    }

    @Override
    public ResponseEntity<NotificationChannelPreferencesDto> updatePreference(Jwt jwt, NotificationType type, NotificationChannel channel, PreferencePatch patch) {
        Long userId = ((Number) jwt.getClaim("user_id")).longValue();
        return ResponseEntity.ok(notificationChannelPreferenceService.toggle(userId, type, channel, patch.enabled()));
    }
}
