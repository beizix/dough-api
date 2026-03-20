package io.dough.api.useCases.user.profile.retrieveProfile.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.profile.retrieveProfile.adapters.out.persistence.RetrieveProfilePersistAdapter;
import io.dough.api.useCases.user.profile.retrieveProfile.application.port.out.ProfileLoaded;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(RetrieveProfilePersistAdapter.class)
class RetrieveProfilePersistAdapterTest extends DataJpaTestBase {

  @Autowired private RetrieveProfilePersistAdapter adapter;
  @Autowired private UserRepository userRepository;

  @Test
  @DisplayName("Scenario: 성공 - 저장된 사용자를 ID로 조회하면 올바른 ProfileLoaded 정보를 반환한다")
  void retrieve_profile_success() {
    // Given
    UserEntity savedUser =
        userRepository.save(
            new UserEntity("test@example.com", "password", "Test User", Role.USER, "token"));

    // When
    ProfileLoaded result = adapter.operate(savedUser.getId());

    // Then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(savedUser.getId());
    assertThat(result.email()).isEqualTo("test@example.com");
    assertThat(result.displayName()).isEqualTo("Test User");
    assertThat(result.role()).isEqualTo(Role.USER);
    assertThat(result.createdAt()).isNotNull();
    assertThat(result.profileImageId()).isNull();
  }
}
