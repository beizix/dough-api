package io.dough.api.useCases.user.mgmt.registerManager.adapters.in.web;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.registerManager.application.port.in.ManagerRegistered;
import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterManagerResponse(
    UUID id, String email, String displayName, Role role, LocalDateTime createdAt) {
  public static RegisterManagerResponse from(ManagerRegistered domain) {
    return new RegisterManagerResponse(
        domain.id(), domain.email(), domain.displayName(), domain.role(), domain.createdAt());
  }
}
