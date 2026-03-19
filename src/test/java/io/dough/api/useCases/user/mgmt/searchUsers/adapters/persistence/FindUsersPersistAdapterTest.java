package io.dough.api.useCases.user.mgmt.searchUsers.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.searchUsers.application.model.SearchUsers;
import io.dough.api.useCases.user.mgmt.searchUsers.application.model.SearchUsersCmd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(FindUsersPersistAdapter.class)
class FindUsersPersistAdapterTest extends DataJpaTestBase {

  @Autowired private UserRepository userRepository;

  @Autowired private FindUsersPersistAdapter findUsersPersistAdapter;

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
    SearchUsersCmd cmd = new SearchUsersCmd("user", null, null, 0, 10, "id: DESC");

    // When
    SearchUsers result = findUsersPersistAdapter.operate(cmd);

    // Then
    assertThat(result.users()).hasSize(2);
    assertThat(result.pageInfo().totalElements()).isEqualTo(2);
  }

  @Test
  @DisplayName("Scenario: 성공 - 이름 LIKE 필터가 정상 동작한다")
  void get_users_with_display_name_filter() {
    // Given
    SearchUsersCmd cmd = new SearchUsersCmd(null, "관리", null, 0, 10, "id: DESC");

    // When
    SearchUsers result = findUsersPersistAdapter.operate(cmd);

    // Then
    assertThat(result.users()).hasSize(1);
    assertThat(result.users().getFirst().displayName()).isEqualTo("관리자");
  }

  @Test
  @DisplayName("Scenario: 성공 - 역할 필터가 정상 동작한다")
  void get_users_with_role_filter() {
    // Given
    SearchUsersCmd cmd = new SearchUsersCmd(null, null, Role.MANAGER, 0, 10, "id: DESC");

    // When
    SearchUsers result = findUsersPersistAdapter.operate(cmd);

    // Then
    assertThat(result.users()).hasSize(1);
    assertThat(result.users().getFirst().email()).isEqualTo("admin@dough.io");
  }

  @Test
  @DisplayName("Scenario: 성공 - 페이징 정보가 올바르게 반환된다")
  void get_users_with_paging() {
    // Given
    SearchUsersCmd cmd = new SearchUsersCmd(null, null, null, 0, 1, "id: DESC");

    // When
    SearchUsers result = findUsersPersistAdapter.operate(cmd);

    // Then
    assertThat(result.users()).hasSize(1);
    assertThat(result.pageInfo().totalPages()).isEqualTo(3);
    assertThat(result.pageInfo().totalElements()).isEqualTo(3);
  }
}
