package io.dough.api.useCases.user.maintenance.getUser.adapters.web.model;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.getUser.application.model.UserDetail;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetUserResponse(
    UUID id, String email, String displayName, Role role, LocalDateTime createdAt) {
  public static GetUserResponse from(UserDetail domain) {
    return new GetUserResponse(
        domain.id(), domain.email(), domain.displayName(), domain.role(), domain.createdAt());
  }
}
