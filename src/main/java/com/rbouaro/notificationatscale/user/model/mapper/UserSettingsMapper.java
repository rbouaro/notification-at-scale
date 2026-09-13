package com.rbouaro.notificationatscale.user.model.mapper;

import com.rbouaro.notificationatscale.user.model.dto.UserSettingsDto;
import com.rbouaro.notificationatscale.user.model.entity.UserSettings;
import org.springframework.stereotype.Component;

@Component
public class UserSettingsMapper {

    public UserSettingsDto toDto(UserSettings settings) {
        return new UserSettingsDto(settings.isTwoFaEnabled(), settings.getTwoFaMethod(), settings.getLanguage());
    }
}
