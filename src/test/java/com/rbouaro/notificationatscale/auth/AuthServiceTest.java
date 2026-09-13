package com.rbouaro.notificationatscale.auth;

import com.rbouaro.notificationatscale.auth.model.dto.AuthResponse;
import com.rbouaro.notificationatscale.auth.model.dto.LoginRequest;
import com.rbouaro.notificationatscale.auth.model.dto.RegisterRequest;
import com.rbouaro.notificationatscale.auth.model.entity.RefreshToken;
import com.rbouaro.notificationatscale.auth.model.repository.RefreshTokenRepository;
import com.rbouaro.notificationatscale.auth.service.AuthService;
import com.rbouaro.notificationatscale.auth.service.JwtService;
import com.rbouaro.notificationatscale.config.properties.JwtProperties;
import com.rbouaro.notificationatscale.exception.ConflictException;
import com.rbouaro.notificationatscale.user.model.dto.UserCredentials;
import com.rbouaro.notificationatscale.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_returnsAuthResponse_whenRequestIsValid() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("jdoe");
        request.setEmail("jdoe@example.com");
        request.setPassword("password123");

        UUID uuid = UUID.randomUUID();
        UserCredentials credentials = new UserCredentials(1L, uuid, "jdoe", "hashed");

        given(userService.existsByEmail("jdoe@example.com")).willReturn(false);
        given(userService.existsByUsername("jdoe")).willReturn(false);
        given(passwordEncoder.encode("password123")).willReturn("hashed");
        given(userService.register("jdoe", "jdoe@example.com", "hashed")).willReturn(credentials);
        given(jwtService.generateAccessToken(1L, uuid, "jdoe")).willReturn("access_token");
        given(jwtProperties.refreshTokenExpiry()).willReturn(604800L);
        given(jwtProperties.accessTokenExpiry()).willReturn(900L);

        AuthResponse response = authService.register(request);

        assertThat(response.accessToken()).isEqualTo("access_token");
        assertThat(response.expiresIn()).isEqualTo(900L);
        verify(refreshTokenRepository).save(any());
    }

    @Test
    void register_throwsConflict_whenEmailAlreadyTaken() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("jdoe");
        request.setEmail("jdoe@example.com");
        request.setPassword("password123");

        given(userService.existsByEmail("jdoe@example.com")).willReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void register_throwsConflict_whenUsernameAlreadyTaken() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("jdoe");
        request.setEmail("jdoe@example.com");
        request.setPassword("password123");

        given(userService.existsByEmail("jdoe@example.com")).willReturn(false);
        given(userService.existsByUsername("jdoe")).willReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void login_returnsAuthResponse_whenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setEmail("jdoe@example.com");
        request.setPassword("password123");

        UUID uuid = UUID.randomUUID();
        UserCredentials credentials = new UserCredentials(1L, uuid, "jdoe", "hashed");

        given(userService.findCredentialsByEmail("jdoe@example.com")).willReturn(Optional.of(credentials));
        given(passwordEncoder.matches("password123", "hashed")).willReturn(true);
        given(jwtService.generateAccessToken(1L, uuid, "jdoe")).willReturn("access_token");
        given(jwtProperties.refreshTokenExpiry()).willReturn(604800L);
        given(jwtProperties.accessTokenExpiry()).willReturn(900L);

        AuthResponse response = authService.login(request);

        assertThat(response.accessToken()).isEqualTo("access_token");
        verify(refreshTokenRepository).revokeAllByUserId(1L);
        verify(refreshTokenRepository).save(any());
    }

    @Test
    void login_throwsUnauthorized_whenUserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ghost@example.com");
        request.setPassword("password123");

        given(userService.findCredentialsByEmail("ghost@example.com")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode())
                        .isEqualTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void login_throwsUnauthorized_whenPasswordIsWrong() {
        LoginRequest request = new LoginRequest();
        request.setEmail("jdoe@example.com");
        request.setPassword("wrongpassword");

        UUID uuid = UUID.randomUUID();
        UserCredentials credentials = new UserCredentials(1L, uuid, "jdoe", "hashed");

        given(userService.findCredentialsByEmail("jdoe@example.com")).willReturn(Optional.of(credentials));
        given(passwordEncoder.matches("wrongpassword", "hashed")).willReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode())
                        .isEqualTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void login_throwsUnauthorized_whenPasswordIsWrong_doesNotLeakUserExistence() {
        LoginRequest request = new LoginRequest();
        request.setEmail("jdoe@example.com");
        request.setPassword("wrongpassword");

        UUID uuid = UUID.randomUUID();
        UserCredentials credentials = new UserCredentials(1L, uuid, "jdoe", "hashed");

        given(userService.findCredentialsByEmail("jdoe@example.com")).willReturn(Optional.of(credentials));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Invalid credentials");
    }

    @Test
    void refresh_returnsNewAuthResponse_whenTokenIsValid() {
        UUID uuid = UUID.randomUUID();
        RefreshToken stored = new RefreshToken("valid_token", 1L, Instant.now().plusSeconds(3600));
        UserCredentials credentials = new UserCredentials(1L, uuid, "jdoe", "hashed");

        given(refreshTokenRepository.findByToken("valid_token")).willReturn(Optional.of(stored));
        given(userService.findCredentialsById(1L)).willReturn(Optional.of(credentials));
        given(jwtService.generateAccessToken(1L, uuid, "jdoe")).willReturn("new_access");
        given(jwtProperties.refreshTokenExpiry()).willReturn(604800L);
        given(jwtProperties.accessTokenExpiry()).willReturn(900L);

        AuthResponse response = authService.refresh("valid_token");

        assertThat(response.accessToken()).isEqualTo("new_access");
        assertThat(stored.isRevoked()).isTrue();
        verify(refreshTokenRepository).save(any());
    }

    @Test
    void refresh_throwsUnauthorized_whenTokenNotFound() {
        given(refreshTokenRepository.findByToken("unknown")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refresh("unknown"))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode())
                        .isEqualTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void refresh_throwsUnauthorized_whenTokenIsRevoked() {
        RefreshToken stored = new RefreshToken("token", 1L, Instant.now().plusSeconds(3600));
        stored.revoke();

        given(refreshTokenRepository.findByToken("token")).willReturn(Optional.of(stored));

        assertThatThrownBy(() -> authService.refresh("token"))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode())
                        .isEqualTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void refresh_throwsUnauthorized_whenTokenIsExpired() {
        RefreshToken stored = new RefreshToken("token", 1L, Instant.now().minusSeconds(1));

        given(refreshTokenRepository.findByToken("token")).willReturn(Optional.of(stored));

        assertThatThrownBy(() -> authService.refresh("token"))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode())
                        .isEqualTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void logout_revokesToken_whenTokenIsValid() {
        RefreshToken stored = new RefreshToken("valid_token", 1L, Instant.now().plusSeconds(3600));

        given(refreshTokenRepository.findByToken("valid_token")).willReturn(Optional.of(stored));

        authService.logout("valid_token");

        assertThat(stored.isRevoked()).isTrue();
    }

    @Test
    void logout_throwsUnauthorized_whenTokenIsInvalid() {
        given(refreshTokenRepository.findByToken("bad_token")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.logout("bad_token"))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode())
                        .isEqualTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void logout_throwsUnauthorized_whenTokenIsAlreadyRevoked() {
        RefreshToken stored = new RefreshToken("token", 1L, Instant.now().plusSeconds(3600));
        stored.revoke();

        given(refreshTokenRepository.findByToken("token")).willReturn(Optional.of(stored));

        assertThatThrownBy(() -> authService.logout("token"))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode())
                        .isEqualTo(HttpStatus.UNAUTHORIZED));
    }
}
