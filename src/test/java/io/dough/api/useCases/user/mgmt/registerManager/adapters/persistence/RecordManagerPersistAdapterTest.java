package io.dough.api.useCases.user.mgmt.registerManager.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.registerManager.application.model.ManagerRegistered;
import io.dough.api.useCases.user.mgmt.registerManager.application.model.RegisterManagerCmd;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(RecordManagerPersistAdapter.class)
class RecordManagerPersistAdapterTest extends DataJpaTestBase {

  @Autowired private UserRepository userRepository;

  @Autowired private RecordManagerPersistAdapter createManagerPersistAdapter;

  @Test
  @DisplayName("Scenario: 성공 - 매니저 정보를 저장하고 도메인 모델을 반환한다")
  void save_manager_success() {
    // Given
    RegisterManagerCmd cmd = new RegisterManagerCmd("manager@dough.io", "새매니저", "encodedPassword1");

    // When
    ManagerRegistered result = createManagerPersistAdapter.operate(cmd);

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
    createManagerPersistAdapter.operate(new RegisterManagerCmd(email, "매니저", "password123"));

    // When
    boolean exists = createManagerPersistAdapter.existsByEmailAndRole(email, role);

    // Then
    assertThat(exists).isTrue();
  }
}
