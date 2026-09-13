package com.rbouaro.notificationatscale.user.model.repository;

import com.rbouaro.notificationatscale.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUsername(String username);
}
