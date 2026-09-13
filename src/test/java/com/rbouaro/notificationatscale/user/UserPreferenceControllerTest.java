package com.rbouaro.notificationatscale.user;

import com.rbouaro.notificationatscale.config.security.SecurityConfig;
import com.rbouaro.notificationatscale.user.api.UserPreferenceController;
import com.rbouaro.notificationatscale.user.model.dto.UserPreferencesDto;
import com.rbouaro.notificationatscale.user.model.entity.NotificationChannel;
import com.rbouaro.notificationatscale.user.model.entity.NotificationType;
import com.rbouaro.notificationatscale.user.service.UserPreferenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserPreferenceController.class)
@Import(SecurityConfig.class)
class UserPreferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserPreferenceService userPreferenceService;

    @Test
    void getMyPreferences_returns200_whenAuthenticated() throws Exception {
        UserPreferencesDto dto = new UserPreferencesDto(Map.of(
                NotificationType.TRANSACTIONAL, Map.of(NotificationChannel.EMAIL, true, NotificationChannel.SMS, true)
        ));
        given(userPreferenceService.getByUserId(1L)).willReturn(dto);

        mockMvc.perform(get("/users/me/notification-preferences")
                        .with(jwt().jwt(b -> b.claim("user_id", 1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preferences.TRANSACTIONAL.EMAIL").value(true));
    }

    @Test
    void getMyPreferences_returns401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/users/me/notification-preferences"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updatePreference_returns200_whenAuthenticated() throws Exception {
        UserPreferencesDto dto = new UserPreferencesDto(Map.of(
                NotificationType.PROMOTIONAL, Map.of(NotificationChannel.SMS, false)
        ));
        given(userPreferenceService.toggle(eq(1L), eq(NotificationType.PROMOTIONAL), eq(NotificationChannel.SMS), eq(false)))
                .willReturn(dto);

        mockMvc.perform(patch("/users/me/notification-preferences/PROMOTIONAL/SMS")
                        .with(jwt().jwt(b -> b.claim("user_id", 1)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"enabled": false}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preferences.PROMOTIONAL.SMS").value(false));
    }

    @Test
    void updatePreference_returns401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(patch("/users/me/notification-preferences/PROMOTIONAL/SMS")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"enabled": false}
                                """))
                .andExpect(status().isUnauthorized());
    }
}
