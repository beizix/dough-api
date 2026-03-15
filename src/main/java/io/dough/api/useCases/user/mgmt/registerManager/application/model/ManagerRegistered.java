package io.dough.api.useCases.user.mgmt.registerManager.application.model;

import io.dough.api.useCases.shared.domain.auth.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record ManagerRegistered(
    UUID id, String email, String displayName, Role role, LocalDateTime createdAt) {}
