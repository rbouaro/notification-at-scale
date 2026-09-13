package com.rbouaro.notificationatscale.user;

import com.rbouaro.notificationatscale.config.security.SecurityConfig;
import com.rbouaro.notificationatscale.user.api.UserSettingsController;
import com.rbouaro.notificationatscale.user.model.dto.UserSettingsDto;
import com.rbouaro.notificationatscale.user.model.entity.TwoFaMethod;
import com.rbouaro.notificationatscale.user.service.UserSettingsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserSettingsController.class)
@Import(SecurityConfig.class)
class UserSettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserSettingsService userSettingsService;

    @Test
    void getMySettings_returns200_whenAuthenticated() throws Exception {
        UserSettingsDto dto = new UserSettingsDto(false, null, "en");
        given(userSettingsService.getByUserId(1L)).willReturn(dto);

        mockMvc.perform(get("/users/me/settings")
                        .with(jwt().jwt(builder -> builder.claim("user_id", 1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.twoFaEnabled").value(false))
                .andExpect(jsonPath("$.language").value("en"));
    }

    @Test
    void getMySettings_returns401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/users/me/settings"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateMySettings_returns200_withUpdatedDto() throws Exception {
        UserSettingsDto dto = new UserSettingsDto(true, TwoFaMethod.TOTP, "fr");
        given(userSettingsService.update(eq(1L), any())).willReturn(dto);

        mockMvc.perform(patch("/users/me/settings")
                        .with(jwt().jwt(builder -> builder.claim("user_id", 1)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"twoFaEnabled": true, "twoFaMethod": "TOTP", "language": "fr"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.twoFaEnabled").value(true))
                .andExpect(jsonPath("$.twoFaMethod").value("TOTP"))
                .andExpect(jsonPath("$.language").value("fr"));
    }

    @Test
    void updateMySettings_returns401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(patch("/users/me/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"language": "fr"}
                                """))
                .andExpect(status().isUnauthorized());
    }
}
