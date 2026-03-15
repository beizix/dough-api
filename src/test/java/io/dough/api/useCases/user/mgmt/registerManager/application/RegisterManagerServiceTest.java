package io.dough.api.useCases.user.mgmt.registerManager.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.registerManager.application.model.RegisterManagerCmd;
import io.dough.api.useCases.user.mgmt.registerManager.application.model.ManagerRegistered;
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
class RegisterManagerServiceTest {

  @Mock private RecordManager recordManager;
  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private RegisterManagerService registerManagerService;

  @Test
  @DisplayName("Scenario: 성공 - 매니저 생성 시 비밀번호를 암호화하고 영속성 포트를 호출한다")
  void create_manager_success() {
    // Given
    RegisterManagerCmd cmd = new RegisterManagerCmd("manager@dough.io", "새매니저", "rawPassword1");
    ManagerRegistered expected =
        new ManagerRegistered(
            UUID.randomUUID(), "manager@dough.io", "새매니저", Role.MANAGER, LocalDateTime.now());

    given(recordManager.existsByEmailAndRole(cmd.email(), cmd.role())).willReturn(false);
    given(passwordEncoder.encode("rawPassword1")).willReturn("encodedPassword1");
    given(recordManager.operate(any(RegisterManagerCmd.class))).willReturn(expected);

    // When
    ManagerRegistered actual = registerManagerService.operate(cmd);

    // Then
    assertThat(actual).isEqualTo(expected);
    verify(recordManager).existsByEmailAndRole(cmd.email(), cmd.role());
    verify(passwordEncoder).encode("rawPassword1");
    verify(recordManager).operate(any(RegisterManagerCmd.class));
  }

  @Test
  @DisplayName("Scenario: 실패 - 이미 동일한 이메일과 권한을 가진 매니저가 존재하면 예외가 발생한다")
  void create_manager_fail_already_exists() {
    // Given
    RegisterManagerCmd cmd = new RegisterManagerCmd("manager@dough.io", "새매니저", "rawPassword1");

    given(recordManager.existsByEmailAndRole(cmd.email(), cmd.role())).willReturn(true);

    // When & Then
    assertThatThrownBy(() -> registerManagerService.operate(cmd))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.auth.email_already_exists");
  }
}
