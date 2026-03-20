package io.dough.api.useCases.user.profile.retrieveProfile.application.port.out;

import io.dough.api.useCases.shared.domain.auth.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProfileLoaded(
    UUID id,
    String email,
    String displayName,
    Role role,
    LocalDateTime createdAt,
    UUID profileImageId) {}
