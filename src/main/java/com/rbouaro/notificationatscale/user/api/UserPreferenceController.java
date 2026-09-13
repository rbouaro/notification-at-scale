package com.rbouaro.notificationatscale.user.api;

import com.rbouaro.notificationatscale.user.model.dto.PreferencePatch;
import com.rbouaro.notificationatscale.user.model.dto.UserPreferencesDto;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;
import com.rbouaro.notificationatscale.user.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserPreferenceController implements UserPreferenceApi {

    private final UserPreferenceService userPreferenceService;

    @Override
    public ResponseEntity<UserPreferencesDto> getMyPreferences(Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("user_id")).longValue();
        return ResponseEntity.ok(userPreferenceService.getByUserId(userId));
    }

    @Override
    public ResponseEntity<UserPreferencesDto> updatePreference(Jwt jwt, NotificationType type, NotificationChannel channel, PreferencePatch patch) {
        Long userId = ((Number) jwt.getClaim("user_id")).longValue();
        return ResponseEntity.ok(userPreferenceService.toggle(userId, type, channel, patch.enabled()));
    }
}
