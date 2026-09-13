package com.rbouaro.notificationatscale.user.api;

import com.rbouaro.notificationatscale.user.model.dto.UserSettingsDto;
import com.rbouaro.notificationatscale.user.model.dto.UserSettingsPatch;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/users/me/settings")
public interface UserSettingsApi {

    @GetMapping
    ResponseEntity<UserSettingsDto> getMySettings(@AuthenticationPrincipal Jwt jwt);

    @PatchMapping
    ResponseEntity<UserSettingsDto> updateMySettings(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserSettingsPatch patch);
}
