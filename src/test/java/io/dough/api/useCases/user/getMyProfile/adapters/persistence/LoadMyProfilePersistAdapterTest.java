package io.dough.api.useCases.user.getMyProfile.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.common.adapters.persistence.entity.UserEntity;
import io.dough.api.common.adapters.persistence.repository.UserRepository;
import io.dough.api.common.application.enums.Role;
import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.user.getMyProfile.application.domain.model.MyProfileLoaded;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(LoadMyProfilePersistAdapter.class)
class LoadMyProfilePersistAdapterTest extends DataJpaTestBase {

  @Autowired private LoadMyProfilePersistAdapter adapter;
  @Autowired private UserRepository userRepository;

  @Test
  @DisplayName("Scenario: 성공 - 저장된 사용자를 ID로 조회하면 올바른 MyProfileLoaded 정보를 반환한다")
  void load_my_profile_success() {
    // Given
    UserEntity savedUser =
        userRepository.save(
            new UserEntity("test@example.com", "password", "Test User", Role.USER, "token"));

    // When
    MyProfileLoaded result = adapter.operate(savedUser.getId());

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
