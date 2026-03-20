package io.dough.api.useCases.user.registerUser.application.port.in;

public record RegisteredToken(String accessToken, String refreshToken) {}
