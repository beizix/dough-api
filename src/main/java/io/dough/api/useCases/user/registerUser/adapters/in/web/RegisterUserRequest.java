package io.dough.api.useCases.user.registerUser.adapters.in.web;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterUserRequest(
    @Schema(description = "이메일", example = "user@example.com") String email,
    @Schema(description = "비밀번호", example = "Password123!") String password,
    @Schema(description = "표시 이름", example = "홍길동") String displayName) {}
