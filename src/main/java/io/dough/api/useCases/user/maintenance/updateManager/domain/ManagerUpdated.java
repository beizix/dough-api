package io.dough.api.useCases.user.maintenance.updateManager.domain;

import io.dough.api.useCases.shared.domain.auth.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record ManagerUpdated(
    UUID id, String email, String displayName, Role role, LocalDateTime updatedAt) {}
