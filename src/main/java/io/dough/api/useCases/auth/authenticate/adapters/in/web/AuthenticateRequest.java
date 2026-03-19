package io.dough.api.useCases.auth.authenticate.adapters.in.web;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청")
public record AuthenticateRequest(
    @Schema(description = "사용자 이메일") String email,
    @Schema(description = "사용자 비밀번호") String password) {}
