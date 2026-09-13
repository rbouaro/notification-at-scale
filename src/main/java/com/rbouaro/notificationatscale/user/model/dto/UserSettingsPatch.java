package com.rbouaro.notificationatscale.user.model.dto;

import com.rbouaro.notificationatscale.user.model.entity.TwoFaMethod;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSettingsPatch {
    private Boolean twoFaEnabled;
    private TwoFaMethod twoFaMethod;

    @Size(min = 2, max = 10)
    private String language;
}
