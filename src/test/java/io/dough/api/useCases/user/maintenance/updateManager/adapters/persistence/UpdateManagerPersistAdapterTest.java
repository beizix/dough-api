package io.dough.api.useCases.user.maintenance.updateManager.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.updateManager.application.model.ManagerUpdated;
import io.dough.api.useCases.user.maintenance.updateManager.application.model.UpdateManagerCmd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(UpdateManagerPersistAdapter.class)
class UpdateManagerPersistAdapterTest extends DataJpaTestBase {

  @Autowired private UserRepository userRepository;

  @Autowired private UpdateManagerPersistAdapter updateManagerPersistAdapter;

  @BeforeEach
  void setUp() {
    userRepository.save(
        new UserEntity("manager@dough.io", "oldPass123!", "옛날이름", Role.MANAGER, null));
  }

  @Test
  @DisplayName("Scenario: 성공 - 매니저 정보를 업데이트하고 도메인 모델을 반환한다")
  void update_manager_success() {
    // Given
    UpdateManagerCmd cmd = new UpdateManagerCmd("manager@dough.io", "수정된이름", "newPass123!");

    // When
    ManagerUpdated result = updateManagerPersistAdapter.operate(cmd);

    // Then
    assertThat(result.email()).isEqualTo("manager@dough.io");
    assertThat(result.displayName()).isEqualTo("수정된이름");

    UserEntity updatedUser =
        userRepository.findByEmailAndRole("manager@dough.io", Role.MANAGER).orElseThrow();
    assertThat(updatedUser.getDisplayName()).isEqualTo("수정된이름");
    assertThat(updatedUser.getPassword()).isEqualTo("newPass123!");
  }
}
