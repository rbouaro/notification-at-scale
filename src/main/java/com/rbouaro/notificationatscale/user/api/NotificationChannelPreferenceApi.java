package com.rbouaro.notificationatscale.user.api;

import com.rbouaro.notificationatscale.user.model.dto.NotificationChannelPreferencesDto;
import com.rbouaro.notificationatscale.user.model.dto.PreferencePatch;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users/me/notification-preferences")
public interface NotificationChannelPreferenceApi {

    @GetMapping
    ResponseEntity<NotificationChannelPreferencesDto> getMyPreferences(@AuthenticationPrincipal Jwt jwt);

    @PatchMapping("/{type}/{channel}")
    ResponseEntity<NotificationChannelPreferencesDto> updatePreference(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable NotificationType type,
            @PathVariable NotificationChannel channel,
            @Valid @RequestBody PreferencePatch patch);
}
