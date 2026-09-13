package com.rbouaro.notificationatscale.user.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_users_uuid", columnNames = "uuid"),
            @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
            @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        },
        indexes = @Index(name = "idx_users_uuid", columnList = "uuid")
)
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Setter
    @Column(nullable = false, length = 50)
    private String username;

    @Setter
    @Column(nullable = false, length = 255)
    private String email;

    @Setter
    @Column(name = "display_name", length = 100)
    private String displayName;

    @Setter
    @Column(length = 500)
    private String bio;

    @Setter
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    @PrePersist
    void prePersist() {
        this.uuid = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
