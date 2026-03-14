package io.dough.api.useCases.shared.application.auth;

public record AuthToken(String accessToken, String refreshToken) {}
