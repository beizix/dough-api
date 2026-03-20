package io.dough.api.useCases.user.profile.updateProfileImage.adapters.in.web;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record UpdateProfileImageResponse(
    @Schema(description = "파일 ID") UUID id,
    @Schema(description = "저장된 파일명") String name,
    @Schema(description = "원본 파일명") String originName,
    @Schema(description = "파일 크기") long fileLength,
    @Schema(description = "이미지 접근 URL") String referURL) {}
