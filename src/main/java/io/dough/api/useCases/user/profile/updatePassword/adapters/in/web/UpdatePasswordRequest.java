package io.dough.api.useCases.user.profile.updatePassword.adapters.in.web;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdatePasswordRequest(
    @Schema(description = "현재 패스워드", example = "Password123!") String currentPassword,
    @Schema(description = "신규 패스워드", example = "NewPassword123!") String newPassword,
    @Schema(description = "신규 패스워드 확인", example = "NewPassword123!") String newPasswordConfirm) {}
