package io.dough.api.useCases.auth.authenticate.adapters.in.web;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record AuthenticateResponse(
    @Schema(description = "액세스 토큰") String accessToken,
    @Schema(description = "리프레시 토큰") String refreshToken) {}
