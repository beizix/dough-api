package io.dough.api.useCases.user.registerUser.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(RegisterUserPersistAdapter.class)
class RegisterUserPersistAdapterTest extends DataJpaTestBase {

  @Autowired private RegisterUserPersistAdapter registerUserPersistAdapter;

  @Autowired private UserRepository userRepository;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 정보를 저장하면 UserEntity로 변환되어 DB에 영속화된다")
  void save_user_success() {
    // Given
    String email = "persist@dough.io";
    String password = "encodedPassword";
    String displayName = "Persist User";
    Role role = Role.USER;

    // When
    registerUserPersistAdapter.save(email, password, displayName, role);

    // Then
    var foundUser = userRepository.findByEmailAndRole(email, role);
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getEmail()).isEqualTo(email);
    assertThat(foundUser.get().getDisplayName()).isEqualTo(displayName);
    assertThat(foundUser.get().getRole()).isEqualTo(role);
  }

  @Test
  @DisplayName("Scenario: 성공 - 존재하는 이메일과 권한 조회 시 true를 반환한다")
  void exists_by_email_and_role_true() {
    // Given
    String email = "exists@dough.io";
    registerUserPersistAdapter.save(email, "pass", "User", Role.USER);

    // When
    boolean exists = registerUserPersistAdapter.existsByEmailAndRole(email, Role.USER);

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("Scenario: 성공 - 존재하지 않는 이메일 조회 시 false를 반환한다")
  void exists_by_email_false() {
    // When
    boolean exists = registerUserPersistAdapter.existsByEmailAndRole("notfound@dough.io", Role.USER);

    // Then
    assertThat(exists).isFalse();
  }
}
