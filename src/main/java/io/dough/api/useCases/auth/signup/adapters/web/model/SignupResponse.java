package io.dough.api.useCases.auth.signup.adapters.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 응답")
public record SignupResponse(
    @Schema(description = "액세스 토큰") String accessToken,
    @Schema(description = "리프레시 토큰") String refreshToken) {}
