package com.rbouaro.notificationatscale.user.service;

import com.rbouaro.notificationatscale.exception.UserNotFoundException;
import com.rbouaro.notificationatscale.user.model.dto.UserCredentials;
import com.rbouaro.notificationatscale.user.model.dto.UserProfile;
import com.rbouaro.notificationatscale.user.model.entity.User;
import com.rbouaro.notificationatscale.user.model.mapper.UserMapper;
import com.rbouaro.notificationatscale.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserSettingsService userSettingsService;
    private final UserPreferenceService userPreferenceService;

    public UserProfile findById(UUID uuid) {
        return userRepository.findByUuid(uuid)
                .map(userMapper::toProfile)
                .orElseThrow(() -> new UserNotFoundException("id", uuid.toString()));
    }

    public UserProfile findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toProfile)
                .orElseThrow(() -> new UserNotFoundException("username", username));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<UserCredentials> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toCredentials);
    }

    public Optional<UserCredentials> findCredentialsById(Long id) {
        return userRepository.findById(id).map(this::toCredentials);
    }

    @Transactional
    public UserCredentials register(String username, String email, String passwordHash) {
        User user = userRepository.save(new User(username, email, passwordHash));
        userSettingsService.createDefaults(user.getId());
        userPreferenceService.createDefaults(user.getId());
        return toCredentials(user);
    }

    private UserCredentials toCredentials(User user) {
        return new UserCredentials(user.getId(), user.getUuid(), user.getUsername(), user.getPasswordHash());
    }
}
