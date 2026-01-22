package io.vision.api.common.application.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

  @Test
  @DisplayName("Scenario: 성공 - ROLE_USER는 ACCESS_USER_API 권한을 가진다")
  void role_user_has_user_api_privilege() {
    // Given
    Role role = Role.ROLE_USER;

    // When
    Set<Privilege> privileges = role.getPrivileges();

    // Then
    assertThat(privileges).contains(Privilege.ACCESS_USER_API);
    assertThat(privileges).doesNotContain(Privilege.ACCESS_ADMIN_API);
  }

  @Test
  @DisplayName("Scenario: 성공 - ROLE_ADMIN은 ACCESS_ADMIN_API 권한을 가진다")
  void role_admin_has_admin_api_privilege() {
    // Given
    Role role = Role.ROLE_ADMIN;

    // When
    Set<Privilege> privileges = role.getPrivileges();

    // Then
    assertThat(privileges).contains(Privilege.ACCESS_ADMIN_API);
    assertThat(privileges).doesNotContain(Privilege.ACCESS_USER_API);
  }
}
