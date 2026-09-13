package com.rbouaro.notificationatscale.user.api;

import com.rbouaro.notificationatscale.user.model.dto.UserSettingsDto;
import com.rbouaro.notificationatscale.user.model.dto.UserSettingsPatch;
import com.rbouaro.notificationatscale.user.service.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserSettingsController implements UserSettingsApi {

    private final UserSettingsService userSettingsService;

    @Override
    public ResponseEntity<UserSettingsDto> getMySettings(Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("user_id")).longValue();
        return ResponseEntity.ok(userSettingsService.getByUserId(userId));
    }

    @Override
    public ResponseEntity<UserSettingsDto> updateMySettings(Jwt jwt, UserSettingsPatch patch) {
        Long userId = ((Number) jwt.getClaim("user_id")).longValue();
        return ResponseEntity.ok(userSettingsService.update(userId, patch));
    }
}
