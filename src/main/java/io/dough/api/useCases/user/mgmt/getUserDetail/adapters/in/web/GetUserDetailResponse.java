package io.dough.api.useCases.user.mgmt.getUserDetail.adapters.in.web;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.UserDetailLoaded;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetUserDetailResponse(
    UUID id, String email, String displayName, Role role, LocalDateTime createdAt) {
  public static GetUserDetailResponse from(UserDetailLoaded domain) {
    return new GetUserDetailResponse(
        domain.id(), domain.email(), domain.displayName(), domain.role(), domain.createdAt());
  }
}
