package io.dough.api.useCases.user.profile.updateProfile.adapters.in.web;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateProfileRequest(
    @Schema(description = "이메일", example = "user@example.com") String email,
    @Schema(description = "표시 이름", example = "홍길동") String displayName) {}
