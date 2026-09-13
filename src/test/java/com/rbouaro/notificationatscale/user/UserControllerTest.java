package com.rbouaro.notificationatscale.user;

import com.rbouaro.notificationatscale.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void getById_returns200_whenUserFound() throws Exception {
        UUID id = UUID.randomUUID();
        UserProfile profile = new UserProfile(
                id, "jdoe", "jdoe@example.com", "John Doe", null, null, Instant.now(), null
        );
        given(userService.findById(id)).willReturn(profile);

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.email").value("jdoe@example.com"));
    }

    @Test
    void getById_returns404_whenUserNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        given(userService.findById(id)).willThrow(new UserNotFoundException("id", id.toString()));

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByUsername_returns200_whenUserFound() throws Exception {
        UUID id = UUID.randomUUID();
        UserProfile profile = new UserProfile(
                id, "jdoe", "jdoe@example.com", "John Doe", null, null, Instant.now(), null
        );
        given(userService.findByUsername("jdoe")).willReturn(profile);

        mockMvc.perform(get("/users/by-username/{username}", "jdoe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jdoe"));
    }

    @Test
    void getByUsername_returns404_whenUserNotFound() throws Exception {
        given(userService.findByUsername("ghost"))
                .willThrow(new UserNotFoundException("username", "ghost"));

        mockMvc.perform(get("/users/by-username/{username}", "ghost"))
                .andExpect(status().isNotFound());
    }
}
