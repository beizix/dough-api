package io.dough.api.useCases.auth.logout.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(InvalateTokenPersistAdapter.class)
class InvalateTokenPersistAdapterTest extends DataJpaTestBase {

  @Autowired private InvalateTokenPersistAdapter invalateTokenPersistAdapter;

  @Autowired private UserRepository userRepository;

  @Test
  @DisplayName("Scenario: 성공 - 로그아웃 시 사용자의 리프레시 토큰이 null로 업데이트된다")
  void logout_persistence_success() {
    // Given
    UserEntity user =
        new UserEntity("logout@dough.io", "pass", "User", Role.USER, "existing-refresh-token");
    UserEntity savedUser = userRepository.save(user);
    UUID userId = savedUser.getId();

    // When
    invalateTokenPersistAdapter.operate(userId);

    // Then
    UserEntity updatedUser = userRepository.findById(userId).orElseThrow();
    assertThat(updatedUser.getRefreshToken()).isNull();
  }
}
