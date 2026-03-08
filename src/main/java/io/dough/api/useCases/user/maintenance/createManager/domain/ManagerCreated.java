package io.dough.api.useCases.user.maintenance.createManager.domain;

import io.dough.api.useCases.shared.domain.auth.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record ManagerCreated(
    UUID id, String email, String displayName, Role role, LocalDateTime createdAt) {}
