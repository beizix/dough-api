package io.dough.api.useCases.user.profile.updatePassword.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.profile.updatePassword.application.port.out.Password;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(UpdatePasswordPersistAdapter.class)
class UpdatePasswordPersistAdapterTest extends DataJpaTestBase {

  @Autowired
  private UpdatePasswordPersistAdapter adapter;

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 비밀번호를 조회한다")
  void load_password_success() {
    // Given
    String currentPassword = "old-password";
    UserEntity user = new UserEntity(
        "test@example.com", currentPassword, "Tester", Role.USER, null);
    userRepository.save(user);

    // When
    Password result = adapter.operate(user.getId());

    // Then
    assertThat(result.id()).isEqualTo(user.getId());
    assertThat(result.encodedValue()).isEqualTo(currentPassword);
  }

  @Test
  @DisplayName("Scenario: 성공 - 사용자 비밀번호를 저장한다")
  void save_password_success() {
    // Given
    UserEntity user = new UserEntity(
        "test@example.com", "old-password", "Tester", Role.USER, null);
    userRepository.save(user);
    String newPassword = "new-password";
    Password password = new Password(user.getId(), newPassword);

    // When
    adapter.operate(password);

    // Then
    UserEntity updatedUser = userRepository.findById(user.getId()).orElseThrow();
    assertThat(updatedUser.getPassword()).isEqualTo(newPassword);
  }

  @Test
  @DisplayName("Scenario: 실패 - 존재하지 않는 사용자의 비밀번호 조회 시 예외가 발생한다")
  void load_password_fail_user_not_found() {
    // Given
    UUID nonExistentId = UUID.randomUUID();

    // When & Then
    assertThatThrownBy(() -> adapter.operate(nonExistentId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("User not found");
  }
}
