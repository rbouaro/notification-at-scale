package com.rbouaro.notificationatscale.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRequest {

    @NotBlank
    private String refreshToken;
}
