package com.rbouaro.notificationatscale.user.doc;

import com.rbouaro.notificationatscale.user.UserProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Tag(name = "Users", description = "User profile operations")
public interface UserApi {

    @Operation(summary = "Get user by ID")
    @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserProfile.class)
            )
    )
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    ResponseEntity<UserProfile> getById(@PathVariable("id") UUID id);

    @Operation(summary = "Get user by username")
    @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserProfile.class)
            )
    )
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/by-username/{username}")
    ResponseEntity<UserProfile> getByUsername(@PathVariable("username") String username);
}
