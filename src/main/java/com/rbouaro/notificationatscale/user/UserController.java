package com.rbouaro.notificationatscale.user;

import com.rbouaro.notificationatscale.user.doc.UserApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserProfile> getById(UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Override
    public ResponseEntity<UserProfile> getByUsername(String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }
}
