package io.dough.api.useCases.user.profile.getProfile.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserProfile(
    UUID id,
    String email,
    String displayName,
    LocalDateTime createdAt,
    UUID profileImageId,
    String profileImageUrl
) {}
