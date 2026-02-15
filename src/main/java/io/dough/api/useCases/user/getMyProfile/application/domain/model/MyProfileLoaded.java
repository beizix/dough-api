package io.dough.api.useCases.user.getMyProfile.application.domain.model;

import io.dough.api.common.application.enums.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record MyProfileLoaded(
    UUID id,
    String email,
    String displayName,
    Role role,
    LocalDateTime createdAt,
    UUID profileImageId) {}
