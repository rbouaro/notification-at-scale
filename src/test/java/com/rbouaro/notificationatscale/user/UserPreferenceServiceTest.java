package com.rbouaro.notificationatscale.user;

import com.rbouaro.notificationatscale.user.model.dto.UserPreferencesDto;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;
import com.rbouaro.notificationatscale.user.model.entity.UserPreference;
import com.rbouaro.notificationatscale.user.model.mapper.UserPreferenceMapper;
import com.rbouaro.notificationatscale.user.model.repository.UserPreferenceRepository;
import com.rbouaro.notificationatscale.user.service.UserPreferenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserPreferenceServiceTest {

    @Mock
    private UserPreferenceRepository userPreferenceRepository;

    @Mock
    private UserPreferenceMapper userPreferenceMapper;

    @InjectMocks
    private UserPreferenceService userPreferenceService;

    @Test
    void getByUserId_returnsGroupedDto() {
        List<UserPreference> rows = List.of(
                new UserPreference(1L, NotificationType.TRANSACTIONAL, NotificationChannel.EMAIL),
                new UserPreference(1L, NotificationType.PROMOTIONAL, NotificationChannel.SMS)
        );
        UserPreferencesDto dto = new UserPreferencesDto(Map.of(
                NotificationType.TRANSACTIONAL, Map.of(NotificationChannel.EMAIL, true),
                NotificationType.PROMOTIONAL, Map.of(NotificationChannel.SMS, true)
        ));
        given(userPreferenceRepository.findAllByUserId(1L)).willReturn(rows);
        given(userPreferenceMapper.toDto(rows)).willReturn(dto);

        UserPreferencesDto result = userPreferenceService.getByUserId(1L);

        assertThat(result.preferences()).containsKey(NotificationType.TRANSACTIONAL);
        assertThat(result.preferences().get(NotificationType.TRANSACTIONAL)).containsEntry(NotificationChannel.EMAIL, true);
    }

    @Test
    void toggle_updatesEnabled_andReturnsFullDto() {
        UserPreference pref = new UserPreference(1L, NotificationType.PROMOTIONAL, NotificationChannel.SMS);
        List<UserPreference> allRows = List.of(pref);
        UserPreferencesDto dto = new UserPreferencesDto(Map.of(
                NotificationType.PROMOTIONAL, Map.of(NotificationChannel.SMS, false)
        ));
        given(userPreferenceRepository.findByUserIdAndNotificationTypeAndChannel(
                1L, NotificationType.PROMOTIONAL, NotificationChannel.SMS)).willReturn(Optional.of(pref));
        given(userPreferenceRepository.findAllByUserId(1L)).willReturn(allRows);
        given(userPreferenceMapper.toDto(allRows)).willReturn(dto);

        UserPreferencesDto result = userPreferenceService.toggle(1L, NotificationType.PROMOTIONAL, NotificationChannel.SMS, false);

        assertThat(pref.isEnabled()).isFalse();
        assertThat(result.preferences().get(NotificationType.PROMOTIONAL)).containsEntry(NotificationChannel.SMS, false);
    }

    @Test
    void toggle_throwsNotFound_whenPreferenceMissing() {
        given(userPreferenceRepository.findByUserIdAndNotificationTypeAndChannel(
                99L, NotificationType.TRANSACTIONAL, NotificationChannel.EMAIL)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userPreferenceService.toggle(99L, NotificationType.TRANSACTIONAL, NotificationChannel.EMAIL, false))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createDefaults_savesAllTypesAndChannels() {
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<UserPreference>> captor = ArgumentCaptor.forClass(List.class);

        userPreferenceService.createDefaults(1L);

        verify(userPreferenceRepository).saveAll(captor.capture());
        List<UserPreference> saved = captor.getValue();
        int expectedCount = NotificationType.values().length * NotificationChannel.values().length;
        assertThat(saved).hasSize(expectedCount);
        assertThat(saved).allMatch(UserPreference::isEnabled);
    }
}
