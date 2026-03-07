package io.dough.api.useCases.user.profile.getProfile.application.model;

import io.dough.api.common.domain.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProfileLoaded(
    UUID id,
    String email,
    String displayName,
    Role role,
    LocalDateTime createdAt,
    UUID profileImageId) {}
