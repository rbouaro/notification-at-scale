package com.rbouaro.notificationatscale.user;

import com.rbouaro.notificationatscale.user.model.dto.UserSettingsDto;
import com.rbouaro.notificationatscale.user.model.dto.UserSettingsPatch;
import com.rbouaro.notificationatscale.user.model.entity.TwoFaMethod;
import com.rbouaro.notificationatscale.user.model.entity.UserSettings;
import com.rbouaro.notificationatscale.user.model.mapper.UserSettingsMapper;
import com.rbouaro.notificationatscale.user.model.repository.UserSettingsRepository;
import com.rbouaro.notificationatscale.user.service.UserSettingsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserSettingsServiceTest {

    @Mock
    private UserSettingsRepository userSettingsRepository;

    @Mock
    private UserSettingsMapper userSettingsMapper;

    @InjectMocks
    private UserSettingsService userSettingsService;

    @Test
    void getByUserId_returnsDto_whenSettingsExist() {
        UserSettings settings = new UserSettings(1L);
        UserSettingsDto dto = new UserSettingsDto(false, null, "en");
        given(userSettingsRepository.findByUserId(1L)).willReturn(Optional.of(settings));
        given(userSettingsMapper.toDto(settings)).willReturn(dto);

        UserSettingsDto result = userSettingsService.getByUserId(1L);

        assertThat(result.language()).isEqualTo("en");
        assertThat(result.twoFaEnabled()).isFalse();
    }

    @Test
    void getByUserId_throwsNotFound_whenSettingsMissing() {
        given(userSettingsRepository.findByUserId(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userSettingsService.getByUserId(99L))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void update_appliesLanguagePatch() {
        UserSettings settings = new UserSettings(1L);
        UserSettingsPatch patch = new UserSettingsPatch();
        patch.setLanguage("fr");
        UserSettingsDto dto = new UserSettingsDto(false, null, "fr");
        given(userSettingsRepository.findByUserId(1L)).willReturn(Optional.of(settings));
        given(userSettingsMapper.toDto(settings)).willReturn(dto);

        UserSettingsDto result = userSettingsService.update(1L, patch);

        assertThat(result.language()).isEqualTo("fr");
    }

    @Test
    void update_enablesTwoFaAndSetsMethod() {
        UserSettings settings = new UserSettings(1L);
        UserSettingsPatch patch = new UserSettingsPatch();
        patch.setTwoFaEnabled(true);
        patch.setTwoFaMethod(TwoFaMethod.TOTP);
        UserSettingsDto dto = new UserSettingsDto(true, TwoFaMethod.TOTP, "en");
        given(userSettingsRepository.findByUserId(1L)).willReturn(Optional.of(settings));
        given(userSettingsMapper.toDto(settings)).willReturn(dto);

        UserSettingsDto result = userSettingsService.update(1L, patch);

        assertThat(result.twoFaEnabled()).isTrue();
        assertThat(result.twoFaMethod()).isEqualTo(TwoFaMethod.TOTP);
    }

    @Test
    void update_clearsTwoFaMethod_whenDisablingTwoFa() {
        UserSettings settings = new UserSettings(1L);
        settings.setTwoFaEnabled(true);
        settings.setTwoFaMethod(TwoFaMethod.SMS);
        UserSettingsPatch patch = new UserSettingsPatch();
        patch.setTwoFaEnabled(false);
        UserSettingsDto dto = new UserSettingsDto(false, null, "en");
        given(userSettingsRepository.findByUserId(1L)).willReturn(Optional.of(settings));
        given(userSettingsMapper.toDto(settings)).willReturn(dto);

        UserSettingsDto result = userSettingsService.update(1L, patch);

        assertThat(result.twoFaEnabled()).isFalse();
        assertThat(result.twoFaMethod()).isNull();
    }

    @Test
    void update_throwsNotFound_whenSettingsMissing() {
        given(userSettingsRepository.findByUserId(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userSettingsService.update(99L, new UserSettingsPatch()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createDefaults_savesNewSettings() {
        userSettingsService.createDefaults(1L);

        verify(userSettingsRepository).save(any(UserSettings.class));
    }
}
