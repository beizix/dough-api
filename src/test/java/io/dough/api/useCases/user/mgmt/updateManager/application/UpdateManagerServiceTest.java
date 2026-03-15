package io.dough.api.useCases.user.mgmt.updateManager.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.updateManager.application.model.ManagerUpdated;
import io.dough.api.useCases.user.mgmt.updateManager.application.model.UpdateManagerCmd;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UpdateManagerServiceTest {

  @Mock private UpdateManager updateManager;
  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UpdateManagerService updateManagerService;

  @Test
  @DisplayName("Scenario: 성공 - 매니저 정보 수정 시 비밀번호를 암호화하고 영속성 포트를 호출한다")
  void update_manager_success() {
    // Given
    UpdateManagerCmd cmd = new UpdateManagerCmd("manager@dough.io", "수정된이름", "newPass123!");
    ManagerUpdated expected =
        new ManagerUpdated(
            UUID.randomUUID(), "manager@dough.io", "수정된이름", Role.MANAGER, LocalDateTime.now());

    given(passwordEncoder.encode("newPass123!")).willReturn("encodedPass1");
    given(updateManager.operate(any(UpdateManagerCmd.class))).willReturn(expected);

    // When
    ManagerUpdated actual = updateManagerService.operate(cmd);

    // Then
    assertThat(actual).isEqualTo(expected);
    verify(passwordEncoder).encode("newPass123!");
    verify(updateManager).operate(any(UpdateManagerCmd.class));
  }
}
