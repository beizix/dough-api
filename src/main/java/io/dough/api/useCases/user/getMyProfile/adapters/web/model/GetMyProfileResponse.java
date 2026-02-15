package io.dough.api.useCases.user.getMyProfile.adapters.web.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetMyProfileResponse(
    UUID id,
    String email,
    String displayName,
    LocalDateTime createdAt,
    String profileImageUrl) {}
