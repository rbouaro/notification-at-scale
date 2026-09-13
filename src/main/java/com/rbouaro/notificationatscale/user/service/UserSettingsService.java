package com.rbouaro.notificationatscale.user.service;

import com.rbouaro.notificationatscale.user.model.dto.UserSettingsDto;
import com.rbouaro.notificationatscale.user.model.dto.UserSettingsPatch;
import com.rbouaro.notificationatscale.user.model.entity.UserSettings;
import com.rbouaro.notificationatscale.user.model.mapper.UserSettingsMapper;
import com.rbouaro.notificationatscale.user.model.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final UserSettingsMapper userSettingsMapper;

    public UserSettingsDto getByUserId(Long userId) {
        return userSettingsRepository.findByUserId(userId)
                .map(userSettingsMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Settings not found"));
    }

    @Transactional
    public UserSettingsDto update(Long userId, UserSettingsPatch patch) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Settings not found"));
        if (patch.getTwoFaEnabled() != null) {
            settings.setTwoFaEnabled(patch.getTwoFaEnabled());
            if (!patch.getTwoFaEnabled()) {
                settings.setTwoFaMethod(null);
            }
        }
        if (patch.getTwoFaMethod() != null && settings.isTwoFaEnabled()) {
            settings.setTwoFaMethod(patch.getTwoFaMethod());
        }
        if (patch.getLanguage() != null) {
            settings.setLanguage(patch.getLanguage());
        }
        return userSettingsMapper.toDto(settings);
    }

    @Transactional
    public void createDefaults(Long userId) {
        userSettingsRepository.save(new UserSettings(userId));
    }
}
