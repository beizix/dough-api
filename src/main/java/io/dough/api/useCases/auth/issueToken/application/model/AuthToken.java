package io.dough.api.useCases.auth.issueToken.application.model;

public record AuthToken(String accessToken, String refreshToken) {}
