package io.dough.api.useCases.user.maintenance.createManager.domain;

import io.dough.api.useCases.shared.domain.auth.Role;

public record CreateManagerCmd(String email, String displayName, String password, Role role) {
  public CreateManagerCmd(String email, String displayName, String password) {
    this(email, displayName, password, Role.MANAGER);
  }
}
