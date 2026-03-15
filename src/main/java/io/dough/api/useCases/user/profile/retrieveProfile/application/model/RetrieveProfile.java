package io.dough.api.useCases.user.profile.retrieveProfile.application.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record RetrieveProfile(
    UUID id,
    String email,
    String displayName,
    LocalDateTime createdAt,
    UUID profileImageId,
    String profileImageUrl) {}
