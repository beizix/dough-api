package io.dough.api.useCases.auth.issueToken.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.auth.issueToken.application.port.out.RefreshUserLoaded;
import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(ManageRefreshPersistAdapter.class)
class ManageRefreshPersistAdapterTest extends DataJpaTestBase {

  @Autowired private ManageRefreshPersistAdapter adapter;

  @Autowired private UserRepository userRepository;

  @Test
  @DisplayName("Scenario: 성공 - 리프레시 토큰으로 사용자 정보를 조회한다")
  void load_refresh_user_success() {
    // Given
    String refreshToken = "valid-refresh-token";
    UserEntity user =
        new UserEntity("test@example.com", "password", "Tester", Role.USER, refreshToken);
    userRepository.save(user);

    // When
    Optional<RefreshUserLoaded> result = adapter.loadRefreshUser(refreshToken);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().email()).isEqualTo("test@example.com");
    assertThat(result.get().displayName()).isEqualTo("Tester");
    assertThat(result.get().role()).isEqualTo(Role.USER);
  }

  @Test
  @DisplayName("Scenario: 성공 - 리프레시 토큰을 업데이트한다")
  void update_refresh_token_success() {
    // Given
    UserEntity user = new UserEntity("test@example.com", "password", "Tester", Role.USER, null);
    userRepository.save(user);
    String newRefreshToken = "new-refresh-token";

    // When
    adapter.updateRefreshToken(user.getId(), newRefreshToken);

    // Then
    UserEntity updatedUser = userRepository.findById(user.getId()).orElseThrow();
    assertThat(updatedUser.getRefreshToken()).isEqualTo(newRefreshToken);
  }
}
