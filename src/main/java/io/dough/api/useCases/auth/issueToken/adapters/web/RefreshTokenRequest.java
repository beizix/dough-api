package io.dough.api.useCases.auth.issueToken.adapters.web;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 갱신 요청")
public record RefreshTokenRequest(@Schema(description = "리프레시 토큰") String refreshToken) {}
