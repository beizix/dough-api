package io.dough.api.useCases.user.profile.retrieveProfile.adapters.in.web.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record RetrieveProfileResponse(
    UUID id, String email, String displayName, LocalDateTime createdAt, String profileImageUrl) {}
