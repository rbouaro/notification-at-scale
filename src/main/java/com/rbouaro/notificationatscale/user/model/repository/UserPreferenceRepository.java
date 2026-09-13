package com.rbouaro.notificationatscale.user.model.repository;

import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;
import com.rbouaro.notificationatscale.user.model.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    List<UserPreference> findAllByUserId(Long userId);
    Optional<UserPreference> findByUserIdAndNotificationTypeAndChannel(Long userId, NotificationType type, NotificationChannel channel);
}
