package io.dough.api.useCases.user.maintenance.createManager.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.createManager.application.model.CreateManagerCmd;
import io.dough.api.useCases.user.maintenance.createManager.application.model.ManagerCreated;
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
class CreateManagerServiceTest {

  @Mock private SaveManager saveManager;
  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private CreateManagerService createManagerService;

  @Test
  @DisplayName("Scenario: 성공 - 매니저 생성 시 비밀번호를 암호화하고 영속성 포트를 호출한다")
  void create_manager_success() {
    // Given
    CreateManagerCmd cmd = new CreateManagerCmd("manager@dough.io", "새매니저", "rawPassword1");
    ManagerCreated expected =
        new ManagerCreated(
            UUID.randomUUID(), "manager@dough.io", "새매니저", Role.MANAGER, LocalDateTime.now());

    given(saveManager.existsByEmailAndRole(cmd.email(), cmd.role())).willReturn(false);
    given(passwordEncoder.encode("rawPassword1")).willReturn("encodedPassword1");
    given(saveManager.operate(any(CreateManagerCmd.class))).willReturn(expected);

    // When
    ManagerCreated actual = createManagerService.operate(cmd);

    // Then
    assertThat(actual).isEqualTo(expected);
    verify(saveManager).existsByEmailAndRole(cmd.email(), cmd.role());
    verify(passwordEncoder).encode("rawPassword1");
    verify(saveManager).operate(any(CreateManagerCmd.class));
  }

  @Test
  @DisplayName("Scenario: 실패 - 이미 동일한 이메일과 권한을 가진 매니저가 존재하면 예외가 발생한다")
  void create_manager_fail_already_exists() {
    // Given
    CreateManagerCmd cmd = new CreateManagerCmd("manager@dough.io", "새매니저", "rawPassword1");

    given(saveManager.existsByEmailAndRole(cmd.email(), cmd.role())).willReturn(true);

    // When & Then
    assertThatThrownBy(() -> createManagerService.operate(cmd))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.auth.email_already_exists");
  }
}
