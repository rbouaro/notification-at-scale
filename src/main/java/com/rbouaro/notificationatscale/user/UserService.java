package com.rbouaro.notificationatscale.user;

import com.rbouaro.notificationatscale.exception.UserNotFoundException;
import com.rbouaro.notificationatscale.user.model.UserMapper;
import com.rbouaro.notificationatscale.user.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
}
