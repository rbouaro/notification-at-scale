package com.rbouaro.notificationatscale.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException(String field, String value) {
        super(HttpStatus.NOT_FOUND, "User not found with " + field + ": " + value);
    }
}
