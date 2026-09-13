package com.rbouaro.notificationatscale.user;

import com.rbouaro.notificationatscale.user.model.dto.NotificationChannelPreferencesDto;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannelPreference;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;
import com.rbouaro.notificationatscale.user.model.mapper.NotificationChannelPreferenceMapper;
import com.rbouaro.notificationatscale.user.model.repository.NotificationChannelPreferenceRepository;
import com.rbouaro.notificationatscale.user.service.NotificationChannelPreferenceService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationChannelPreferenceServiceTest {

    @Mock
    private NotificationChannelPreferenceRepository notificationChannelPreferenceRepository;

    @Mock
    private NotificationChannelPreferenceMapper notificationChannelPreferenceMapper;

    @InjectMocks
    private NotificationChannelPreferenceService notificationChannelPreferenceService;

    @Test
    void getByUserId_returnsGroupedDto() {
        List<NotificationChannelPreference> rows = List.of(
                new NotificationChannelPreference(1L, NotificationType.TRANSACTIONAL, NotificationChannel.EMAIL),
                new NotificationChannelPreference(1L, NotificationType.PROMOTIONAL, NotificationChannel.SMS)
        );
        NotificationChannelPreferencesDto dto = new NotificationChannelPreferencesDto(Map.of(
                NotificationType.TRANSACTIONAL, Map.of(NotificationChannel.EMAIL, true),
                NotificationType.PROMOTIONAL, Map.of(NotificationChannel.SMS, true)
        ));
        given(notificationChannelPreferenceRepository.findAllByUserId(1L)).willReturn(rows);
        given(notificationChannelPreferenceMapper.toDto(rows)).willReturn(dto);

        NotificationChannelPreferencesDto result = notificationChannelPreferenceService.getByUserId(1L);

        assertThat(result.preferences()).containsKey(NotificationType.TRANSACTIONAL);
        assertThat(result.preferences().get(NotificationType.TRANSACTIONAL)).containsEntry(NotificationChannel.EMAIL, true);
    }

    @Test
    void toggle_updatesEnabled_andReturnsFullDto() {
        NotificationChannelPreference pref = new NotificationChannelPreference(1L, NotificationType.PROMOTIONAL, NotificationChannel.SMS);
        List<NotificationChannelPreference> allRows = List.of(pref);
        NotificationChannelPreferencesDto dto = new NotificationChannelPreferencesDto(Map.of(
                NotificationType.PROMOTIONAL, Map.of(NotificationChannel.SMS, false)
        ));
        given(notificationChannelPreferenceRepository.findByUserIdAndNotificationTypeAndChannel(
                1L, NotificationType.PROMOTIONAL, NotificationChannel.SMS)).willReturn(Optional.of(pref));
        given(notificationChannelPreferenceRepository.findAllByUserId(1L)).willReturn(allRows);
        given(notificationChannelPreferenceMapper.toDto(allRows)).willReturn(dto);

        NotificationChannelPreferencesDto result = notificationChannelPreferenceService.toggle(1L, NotificationType.PROMOTIONAL, NotificationChannel.SMS, false);

        assertThat(pref.isEnabled()).isFalse();
        assertThat(result.preferences().get(NotificationType.PROMOTIONAL)).containsEntry(NotificationChannel.SMS, false);
    }

    @Test
    void toggle_throwsNotFound_whenPreferenceMissing() {
        given(notificationChannelPreferenceRepository.findByUserIdAndNotificationTypeAndChannel(
                99L, NotificationType.TRANSACTIONAL, NotificationChannel.EMAIL)).willReturn(Optional.empty());

        assertThatThrownBy(() -> notificationChannelPreferenceService.toggle(99L, NotificationType.TRANSACTIONAL, NotificationChannel.EMAIL, false))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createDefaults_savesAllTypesAndChannels() {
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<NotificationChannelPreference>> captor = ArgumentCaptor.forClass(List.class);

        notificationChannelPreferenceService.createDefaults(1L);

        verify(notificationChannelPreferenceRepository).saveAll(captor.capture());
        List<NotificationChannelPreference> saved = captor.getValue();
        int expectedCount = NotificationType.values().length * NotificationChannel.values().length;
        assertThat(saved).hasSize(expectedCount);
        assertThat(saved).allMatch(NotificationChannelPreference::isEnabled);
    }
}
