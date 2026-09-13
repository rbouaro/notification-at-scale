package com.rbouaro.notificationatscale.user.model.mapper;

import com.rbouaro.notificationatscale.user.model.dto.UserPreferencesDto;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;
import com.rbouaro.notificationatscale.user.model.entity.UserPreference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserPreferenceMapper {

    public UserPreferencesDto toDto(List<UserPreference> preferences) {
        Map<NotificationType, Map<NotificationChannel, Boolean>> grouped = preferences.stream()
                .collect(Collectors.groupingBy(
                        UserPreference::getNotificationType,
                        Collectors.toMap(UserPreference::getChannel, UserPreference::isEnabled)
                ));
        return new UserPreferencesDto(grouped);
    }
}
