package io.dough.api.useCases.user.registerUser.adapters.in.web;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterUserResponse(
    @Schema(description = "Access Token", example = "eyJhbG...") String accessToken,
    @Schema(description = "Refresh Token", example = "eyJhbG...") String refreshToken) {}
