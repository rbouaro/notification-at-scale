package com.rbouaro.notificationatscale.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
        }
)
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public User(String username, String email) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.email = email;
    }
}
