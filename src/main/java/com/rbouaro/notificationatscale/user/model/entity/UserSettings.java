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
@Table(name = "user_settings")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Setter
    @Column(name = "two_fa_enabled", nullable = false)
    private boolean twoFaEnabled = false;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "two_fa_method", length = 10)
    private TwoFaMethod twoFaMethod;

    @Setter
    @Column(nullable = false, length = 10)
    private String language = "en";

    public UserSettings(Long userId) {
        this.userId = userId;
    }
}
