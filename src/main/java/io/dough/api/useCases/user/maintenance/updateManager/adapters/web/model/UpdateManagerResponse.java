package io.dough.api.useCases.user.maintenance.updateManager.adapters.web.model;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.updateManager.domain.ManagerUpdated;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateManagerResponse(
    UUID id, String email, String displayName, Role role, LocalDateTime updatedAt) {
  public static UpdateManagerResponse from(ManagerUpdated domain) {
    return new UpdateManagerResponse(
        domain.id(), domain.email(), domain.displayName(), domain.role(), domain.updatedAt());
  }
}
