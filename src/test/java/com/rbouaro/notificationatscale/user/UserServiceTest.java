package com.rbouaro.notificationatscale.user;

import com.rbouaro.notificationatscale.exception.UserNotFoundException;
import com.rbouaro.notificationatscale.user.model.dto.UserProfile;
import com.rbouaro.notificationatscale.user.model.entity.User;
import com.rbouaro.notificationatscale.user.model.mapper.UserMapper;
import com.rbouaro.notificationatscale.user.model.repository.UserRepository;
import com.rbouaro.notificationatscale.user.service.UserService;
import com.rbouaro.notificationatscale.user.service.UserSettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserSettingsService userSettingsService;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserProfile profile;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        user = new User("jdoe", "jdoe@example.com", "hashed_password");
        profile = new UserProfile(uuid, "jdoe", "jdoe@example.com", null, null, null, Instant.now(), null);
    }

    @Test
    void findById_returnsProfile_whenUserExists() {
        given(userRepository.findByUuid(uuid)).willReturn(Optional.of(user));
        given(userMapper.toProfile(user)).willReturn(profile);

        UserProfile result = userService.findById(uuid);

        assertThat(result.username()).isEqualTo("jdoe");
        assertThat(result.email()).isEqualTo("jdoe@example.com");
    }

    @Test
    void findById_throwsNotFound_whenUserMissing() {
        given(userRepository.findByUuid(uuid)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(uuid))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void findByUsername_returnsProfile_whenUserExists() {
        given(userRepository.findByUsername("jdoe")).willReturn(Optional.of(user));
        given(userMapper.toProfile(user)).willReturn(profile);

        UserProfile result = userService.findByUsername("jdoe");

        assertThat(result.username()).isEqualTo("jdoe");
        assertThat(result.email()).isEqualTo("jdoe@example.com");
    }

    @Test
    void findByUsername_throwsNotFound_whenUserMissing() {
        given(userRepository.findByUsername("ghost")).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByUsername("ghost"))
                .isInstanceOf(UserNotFoundException.class);
    }
}
