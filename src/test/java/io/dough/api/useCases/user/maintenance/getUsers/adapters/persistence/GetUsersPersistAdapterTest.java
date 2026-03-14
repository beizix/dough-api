package io.dough.api.useCases.user.maintenance.getUsers.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.getUsers.application.model.GetUsers;
import io.dough.api.useCases.user.maintenance.getUsers.application.model.GetUsersCmd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(GetUsersPersistAdapter.class)
class GetUsersPersistAdapterTest extends DataJpaTestBase {

  @Autowired private UserRepository userRepository;

  @Autowired private GetUsersPersistAdapter getUsersPersistAdapter;

  @BeforeEach
  void setUp() {
    userRepository.save(new UserEntity("admin@dough.io", "pass", "관리자", Role.MANAGER, null));
    userRepository.save(new UserEntity("user1@dough.io", "pass", "사용자1", Role.USER, null));
    userRepository.save(new UserEntity("user2@dough.io", "pass", "사용자2", Role.USER, null));
  }

  @Test
  @DisplayName("Scenario: 성공 - 이메일 LIKE 필터가 정상 동작한다")
  void get_users_with_email_filter() {
    // Given
    GetUsersCmd cmd = new GetUsersCmd("user", null, null, 0, 10, "id: DESC");

    // When
    GetUsers result = getUsersPersistAdapter.operate(cmd);

    // Then
    assertThat(result.users()).hasSize(2);
    assertThat(result.pageInfo().totalElements()).isEqualTo(2);
  }

  @Test
  @DisplayName("Scenario: 성공 - 이름 LIKE 필터가 정상 동작한다")
  void get_users_with_display_name_filter() {
    // Given
    GetUsersCmd cmd = new GetUsersCmd(null, "관리", null, 0, 10, "id: DESC");

    // When
    GetUsers result = getUsersPersistAdapter.operate(cmd);

    // Then
    assertThat(result.users()).hasSize(1);
    assertThat(result.users().getFirst().displayName()).isEqualTo("관리자");
  }

  @Test
  @DisplayName("Scenario: 성공 - 역할 필터가 정상 동작한다")
  void get_users_with_role_filter() {
    // Given
    GetUsersCmd cmd = new GetUsersCmd(null, null, Role.MANAGER, 0, 10, "id: DESC");

    // When
    GetUsers result = getUsersPersistAdapter.operate(cmd);

    // Then
    assertThat(result.users()).hasSize(1);
    assertThat(result.users().getFirst().email()).isEqualTo("admin@dough.io");
  }

  @Test
  @DisplayName("Scenario: 성공 - 페이징 정보가 올바르게 반환된다")
  void get_users_with_paging() {
    // Given
    GetUsersCmd cmd = new GetUsersCmd(null, null, null, 0, 1, "id: DESC");

    // When
    GetUsers result = getUsersPersistAdapter.operate(cmd);

    // Then
    assertThat(result.users()).hasSize(1);
    assertThat(result.pageInfo().totalPages()).isEqualTo(3);
    assertThat(result.pageInfo().totalElements()).isEqualTo(3);
  }
}
