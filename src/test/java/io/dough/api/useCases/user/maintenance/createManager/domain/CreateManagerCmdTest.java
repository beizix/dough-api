package io.dough.api.useCases.user.maintenance.createManager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CreateManagerCmdTest {

  @Test
  @DisplayName("Scenario: 성공 - 유효한 비밀번호 입력 시 커맨드 객체가 생성된다")
  void create_command_success() {
    // Given
    String email = "manager@dough.io";
    String displayName = "매니저";
    String validPassword = "password123!";

    // When
    CreateManagerCmd cmd = new CreateManagerCmd(email, displayName, validPassword);

    // Then
    assertThat(cmd.password()).isEqualTo(validPassword);
  }

  @ParameterizedTest
  @ValueSource(strings = {"short1", "onlyalpha", "12345678", ""})
  @DisplayName("Scenario: 실패 - 비밀번호 정책 위반 시 예외가 발생한다")
  void create_command_fail_invalid_password(String invalidPassword) {
    // Given
    String email = "manager@dough.io";
    String displayName = "매니저";

    // Then
    assertThatThrownBy(() -> new CreateManagerCmd(email, displayName, invalidPassword))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("비밀번호는 영문과 숫자를 포함하여 최소 8자 이상이어야 합니다.");
  }

  @Test
  @DisplayName("Scenario: 실패 - 비밀번호가 null일 경우 예외가 발생한다")
  void create_command_fail_null_password() {
    // Then
    assertThatThrownBy(() -> new CreateManagerCmd("manager@dough.io", "매니저", null))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
