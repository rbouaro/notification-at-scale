package com.rbouaro.notificationatscale.user.model.mapper;

import com.rbouaro.notificationatscale.user.model.dto.NotificationChannelPreferencesDto;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannelPreference;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NotificationChannelPreferenceMapper {

    public NotificationChannelPreferencesDto toDto(List<NotificationChannelPreference> preferences) {
        Map<NotificationType, Map<NotificationChannel, Boolean>> grouped = preferences.stream()
                .collect(Collectors.groupingBy(
                        NotificationChannelPreference::getNotificationType,
                        Collectors.toMap(NotificationChannelPreference::getChannel, NotificationChannelPreference::isEnabled)
                ));
        return new NotificationChannelPreferencesDto(grouped);
    }
}
