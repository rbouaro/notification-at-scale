package com.rbouaro.notificationatscale.user.service;

import com.rbouaro.notificationatscale.user.model.dto.UserPreferencesDto;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;
import com.rbouaro.notificationatscale.user.model.entity.UserPreference;
import com.rbouaro.notificationatscale.user.model.mapper.UserPreferenceMapper;
import com.rbouaro.notificationatscale.user.model.repository.UserPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserPreferenceMapper userPreferenceMapper;

    public UserPreferencesDto getByUserId(Long userId) {
        return userPreferenceMapper.toDto(userPreferenceRepository.findAllByUserId(userId));
    }

    @Transactional
    public UserPreferencesDto toggle(Long userId, NotificationType type, NotificationChannel channel, boolean enabled) {
        UserPreference preference = userPreferenceRepository
                .findByUserIdAndNotificationTypeAndChannel(userId, type, channel)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Preference not found"));
        preference.setEnabled(enabled);
        return getByUserId(userId);
    }

    @Transactional
    public void createDefaults(Long userId) {
        List<UserPreference> defaults = new ArrayList<>();
        for (NotificationType type : NotificationType.values()) {
            for (NotificationChannel channel : NotificationChannel.values()) {
                defaults.add(new UserPreference(userId, type, channel));
            }
        }
        userPreferenceRepository.saveAll(defaults);
    }
}
