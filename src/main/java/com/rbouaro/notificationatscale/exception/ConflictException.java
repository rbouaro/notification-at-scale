package com.rbouaro.notificationatscale.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException extends ResponseStatusException {

    public ConflictException(String field, String value) {
        super(HttpStatus.CONFLICT, field + " already exists: " + value);
    }
}
