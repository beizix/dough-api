package io.dough.api.useCases.user.profile.retrieveProfile.application.port.in;

import java.time.LocalDateTime;
import java.util.UUID;

public record RetrieveProfile(
    UUID id,
    String email,
    String displayName,
    LocalDateTime createdAt,
    UUID profileImageId,
    String profileImageUrl) {}
