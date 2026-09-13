package com.rbouaro.notificationatscale.auth;

import com.rbouaro.notificationatscale.auth.api.AuthController;
import com.rbouaro.notificationatscale.auth.model.dto.AuthResponse;
import com.rbouaro.notificationatscale.auth.service.AuthService;
import com.rbouaro.notificationatscale.config.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void register_returns201_whenRequestIsValid() throws Exception {
        AuthResponse response = AuthResponse.builder()
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .expiresIn(900L)
                .build();
        given(authService.register(any())).willReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"jdoe","email":"jdoe@example.com","password":"password123"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("access_token"))
                .andExpect(jsonPath("$.expiresIn").value(900));
    }

    @Test
    void register_returns400_whenRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"ab","email":"not-an-email","password":"short"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_returns200_whenCredentialsAreValid() throws Exception {
        AuthResponse response = AuthResponse.builder()
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .expiresIn(900L)
                .build();
        given(authService.login(any())).willReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"jdoe@example.com","password":"password123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access_token"));
    }

    @Test
    void login_returns401_whenCredentialsAreInvalid() throws Exception {
        given(authService.login(any()))
                .willThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"jdoe@example.com","password":"wrongpassword"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refresh_returns200_whenTokenIsValid() throws Exception {
        AuthResponse response = AuthResponse.builder()
                .accessToken("new_access")
                .refreshToken("new_refresh")
                .expiresIn(900L)
                .build();
        given(authService.refresh(anyString())).willReturn(response);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refreshToken":"some_valid_token"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new_access"))
                .andExpect(jsonPath("$.refreshToken").value("new_refresh"));
    }

    @Test
    void refresh_returns401_whenTokenIsInvalid() throws Exception {
        given(authService.refresh(anyString()))
                .willThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refreshToken":"bad_token"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_returns204_whenTokenIsValid() throws Exception {
        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refreshToken":"some_valid_token"}
                                """))
                .andExpect(status().isNoContent());
    }

    @Test
    void logout_returns401_whenTokenIsInvalid() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"))
                .when(authService).logout(anyString());

        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refreshToken":"bad_token"}
                                """))
                .andExpect(status().isUnauthorized());
    }
}
