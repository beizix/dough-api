package io.dough.api.useCases.user.profile.updatePassword.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.user.profile.updatePassword.application.port.in.UpdatePasswordCmd;
import io.dough.api.useCases.user.profile.updatePassword.application.port.out.LoadPassword;
import io.dough.api.useCases.user.profile.updatePassword.application.port.out.Password;
import io.dough.api.useCases.user.profile.updatePassword.application.port.out.SavePassword;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UpdatePasswordServiceTest {

  @Mock private LoadPassword loadPassword;
  @Mock private SavePassword savePassword;
  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UpdatePasswordService updatePasswordService;

  @Test
  @DisplayName("Scenario: 실패 - 신규 패스워드와 확인 패스워드가 일치하지 않으면 예외가 발생한다")
  void update_password_mismatch_fail() {
    // When & Then
    assertThatThrownBy(
            () -> new UpdatePasswordCmd(UUID.randomUUID(), "current", "newPass123!", "mismatch"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("Scenario: 실패 - 현재 패스워드가 올바르지 않으면 예외가 발생한다")
  void update_password_current_incorrect_fail() {
    // Given
    UUID userId = UUID.randomUUID();
    UpdatePasswordCmd cmd =
        new UpdatePasswordCmd(userId, "wrongCurrent", "newPass123!", "newPass123!");
    Password domainModel = new Password(userId, "encodedCurrent");

    given(loadPassword.operate(userId)).willReturn(domainModel);
    given(passwordEncoder.matches("wrongCurrent", "encodedCurrent")).willReturn(false);

    // When & Then
    assertThatThrownBy(() -> updatePasswordService.operate(cmd))
        .isInstanceOf(IllegalArgumentException.class);

    verify(savePassword, never()).operate(any());
  }

  @Test
  @DisplayName("Scenario: 성공 - 모든 조건 만족 시 패스워드가 업데이트되고 저장된다")
  void update_password_success() {
    // Given
    UUID userId = UUID.randomUUID();
    UpdatePasswordCmd cmd = new UpdatePasswordCmd(userId, "current", "newPass123!", "newPass123!");
    Password domainModel = new Password(userId, "encodedCurrent");

    given(loadPassword.operate(userId)).willReturn(domainModel);
    given(passwordEncoder.matches("current", "encodedCurrent")).willReturn(true);
    given(passwordEncoder.encode("newPass123!")).willReturn("encodedNew");

    // When
    updatePasswordService.operate(cmd);

    // Then
    verify(savePassword).operate(any(Password.class));
  }
}
