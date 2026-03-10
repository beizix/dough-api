package io.dough.api.useCases.user.maintenance.createManager.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.createManager.domain.CreateManagerCmd;
import io.dough.api.useCases.user.maintenance.createManager.domain.ManagerCreated;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(CreateManagerPersistAdapter.class)
class CreateManagerPersistAdapterTest extends DataJpaTestBase {

  @Autowired private UserRepository userRepository;

  @Autowired private CreateManagerPersistAdapter createManagerPersistAdapter;

  @Test
  @DisplayName("Scenario: 성공 - 매니저 정보를 저장하고 도메인 모델을 반환한다")
  void save_manager_success() {
    // Given
    CreateManagerCmd cmd = new CreateManagerCmd("manager@dough.io", "새매니저", "encodedPassword1");

    // When
    ManagerCreated result = createManagerPersistAdapter.operate(cmd);

    // Then
    assertThat(result.id()).isNotNull();
    assertThat(result.email()).isEqualTo("manager@dough.io");
    assertThat(result.role()).isEqualTo(Role.MANAGER);
    assertThat(userRepository.findByEmailAndRole("manager@dough.io", Role.MANAGER)).isPresent();
  }

  @Test
  @DisplayName("Scenario: 성공 - 이메일과 권한으로 사용자가 존재하는지 확인한다")
  void exists_by_email_and_role_success() {
    // Given
    String email = "manager@dough.io";
    Role role = Role.MANAGER;
    createManagerPersistAdapter.operate(new CreateManagerCmd(email, "매니저", "password123"));

    // When
    boolean exists = createManagerPersistAdapter.existsByEmailAndRole(email, role);

    // Then
    assertThat(exists).isTrue();
  }
}
