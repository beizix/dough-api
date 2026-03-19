package io.dough.api.useCases.auth.issueToken.application.port.in;

public record AuthToken(String accessToken, String refreshToken) {}
