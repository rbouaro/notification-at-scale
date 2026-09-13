package com.rbouaro.notificationatscale.user.service;

import com.rbouaro.notificationatscale.user.model.dto.NotificationChannelPreferencesDto;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannelPreference;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;
import com.rbouaro.notificationatscale.user.model.mapper.NotificationChannelPreferenceMapper;
import com.rbouaro.notificationatscale.user.model.repository.NotificationChannelPreferenceRepository;
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
public class NotificationChannelPreferenceService {

    private final NotificationChannelPreferenceRepository notificationChannelPreferenceRepository;
    private final NotificationChannelPreferenceMapper notificationChannelPreferenceMapper;

    public NotificationChannelPreferencesDto getByUserId(Long userId) {
        return notificationChannelPreferenceMapper.toDto(
                notificationChannelPreferenceRepository.findAllByUserId(userId));
    }

    @Transactional
    public NotificationChannelPreferencesDto toggle(Long userId, NotificationType type, NotificationChannel channel, boolean enabled) {
        NotificationChannelPreference preference = notificationChannelPreferenceRepository
                .findByUserIdAndNotificationTypeAndChannel(userId, type, channel)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Preference not found"));
        preference.setEnabled(enabled);
        return getByUserId(userId);
    }

    @Transactional
    public void createDefaults(Long userId) {
        List<NotificationChannelPreference> defaults = new ArrayList<>();
        for (NotificationType type : NotificationType.values()) {
            for (NotificationChannel channel : NotificationChannel.values()) {
                defaults.add(new NotificationChannelPreference(userId, type, channel));
            }
        }
        notificationChannelPreferenceRepository.saveAll(defaults);
    }
}
