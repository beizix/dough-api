package io.dough.api.useCases.user.getMyProfile.adapters.web.model;

import io.dough.api.common.application.enums.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetMyProfileResponse(
    UUID id,
    String email,
    String displayName,
    Role role,
    LocalDateTime createdAt,
    UUID profileImageId,
    String profileImageUrl) {}
