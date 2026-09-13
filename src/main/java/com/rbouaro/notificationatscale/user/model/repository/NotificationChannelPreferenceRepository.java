package com.rbouaro.notificationatscale.user.model.repository;

import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannelPreference;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationChannelPreferenceRepository extends JpaRepository<NotificationChannelPreference, Long> {
    List<NotificationChannelPreference> findAllByUserId(Long userId);
    Optional<NotificationChannelPreference> findByUserIdAndNotificationTypeAndChannel(Long userId, NotificationType type, NotificationChannel channel);
}
