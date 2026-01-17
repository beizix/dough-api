package io.vision.api.useCases.auth.application.model;

public record AuthToken(String accessToken, String refreshToken) {}
