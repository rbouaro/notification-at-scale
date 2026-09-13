package com.rbouaro.notificationatscale.user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "notification_channel_preferences")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class NotificationChannelPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, updatable = false, length = 20)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = 20)
    private NotificationChannel channel;

    @Setter
    @Column(nullable = false)
    private boolean enabled = true;

    public NotificationChannelPreference(Long userId, NotificationType notificationType, NotificationChannel channel) {
        this.userId = userId;
        this.notificationType = notificationType;
        this.channel = channel;
        this.enabled = true;
    }
}
