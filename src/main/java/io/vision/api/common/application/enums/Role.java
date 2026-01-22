package io.vision.api.common.application.enums;

import java.util.Set;

public enum Role {
  ROLE_USER(Set.of(Privilege.ACCESS_USER_API)),
  ROLE_ADMIN(Set.of(Privilege.ACCESS_ADMIN_API));

  private final Set<Privilege> privileges;

  Role(Set<Privilege> privileges) {
    this.privileges = privileges;
  }

  public Set<Privilege> getPrivileges() {
    return privileges;
  }
}
