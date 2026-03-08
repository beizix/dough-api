package io.dough.api.useCases.user.maintenance.createManager.adapters.web.model;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.createManager.domain.ManagerCreated;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateManagerResponse(
    UUID id, String email, String displayName, Role role, LocalDateTime createdAt) {
  public static CreateManagerResponse from(ManagerCreated domain) {
    return new CreateManagerResponse(
        domain.id(), domain.email(), domain.displayName(), domain.role(), domain.createdAt());
  }
}
