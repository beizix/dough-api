package io.dough.api.useCases.user.profile.updatePassword.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import io.dough.api.useCases.user.profile.updatePassword.application.model.UpdatePasswordCmd;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class UpdatePasswordTest {

  @Nested
  @DisplayName("Scenario: UpdatePasswordCmd 자가 검증 테스트")
  class UpdatePasswordCmdValidationTest {

    @Test
    @DisplayName("성공: 새 비밀번호와 확인 비밀번호가 일치하면 객체가 생성된다")
    void constructor_success_when_passwords_match() {
      // Given
      UUID userId = UUID.randomUUID();
      String currentPass = "oldPass";
      String newPass = "newPass123";

      // When
      UpdatePasswordCmd cmd = new UpdatePasswordCmd(userId, currentPass, newPass, newPass);

      // Then
      assertThat(cmd.newPassword()).isEqualTo(newPass);
    }

    @Test
    @DisplayName("실패: 새 비밀번호와 확인 비밀번호가 다르면 'error.password.mismatch' 예외가 발생한다")
    void constructor_fail_when_passwords_mismatch() {
      // Given
      UUID userId = UUID.randomUUID();
      String currentPass = "oldPass";
      String newPass = "newPass123";
      String confirmPass = "differentPass";

      // When & Then
      assertThatThrownBy(() -> new UpdatePasswordCmd(userId, currentPass, newPass, confirmPass))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("error.password.mismatch");
    }
  }

  @Nested
  @DisplayName("Scenario: UpdatePassword 도메인 행위 테스트")
  class UpdatePasswordBehaviorTest {

    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final UUID id = UUID.randomUUID();
    private final String encodedPassword = "encodedOldPass";
    private final UpdatedPassword domain = new UpdatedPassword(id, encodedPassword);

    @Test
    @DisplayName("verify 성공: 원문 비밀번호가 인코딩된 비밀번호와 일치하면 통과한다")
    void verify_success_when_password_matches() {
      // Given
      String rawPassword = "oldPass";
      given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);

      // When & Then (No exception expected)
      domain.verify(rawPassword, passwordEncoder);
    }

    @Test
    @DisplayName("verify 실패: 비밀번호가 일치하지 않으면 'error.password.current.incorrect' 예외가 발생한다")
    void verify_fail_when_password_mismatches() {
      // Given
      String rawPassword = "wrongPass";
      given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(false);

      // When & Then
      assertThatThrownBy(() -> domain.verify(rawPassword, passwordEncoder))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("error.password.current.incorrect");
    }

    @Test
    @DisplayName("update 성공: 새 비밀번호를 암호화하여 새로운 상태를 가진 도메인 객체를 반환한다")
    void update_success_returns_new_domain_with_encoded_password() {
      // Given
      String newRawPassword = "newPass123";
      String newEncodedPassword = "encodedNewPass";
      given(passwordEncoder.encode(newRawPassword)).willReturn(newEncodedPassword);

      // When
      UpdatedPassword updated = domain.update(newRawPassword, passwordEncoder);

      // Then
      assertThat(updated.id()).isEqualTo(id);
      assertThat(updated.encodedPassword()).isEqualTo(newEncodedPassword);
      assertThat(updated).isNotSameAs(domain);
    }
  }
}
