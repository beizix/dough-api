package io.dough.api.useCases.auth.authenticate.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.auth.authenticate.application.LoadAuthenticatableUser;
import io.dough.api.useCases.auth.authenticate.application.AuthenticateService;
import io.dough.api.useCases.auth.authenticate.application.model.AuthenticateCmd;
import io.dough.api.useCases.auth.authenticate.application.model.AuthenticatedToken;
import io.dough.api.useCases.auth.authenticate.domain.AuthenticatableUser;
import io.dough.api.useCases.auth.issueToken.application.IssueTokenUseCase;
import io.dough.api.useCases.auth.issueToken.application.model.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.model.IssueTokenCmd;
import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticateServiceTest {

  @InjectMocks private AuthenticateService authenticateService;

  @Mock private LoadAuthenticatableUser loadAuthenticatableUser;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private IssueTokenUseCase issueTokenUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 유효한 자격 증명으로 로그인 성공")
  void operate_success() {
    // Given
    String email = "test@example.com";
    String password = "password123!";
    String encodedPassword = "encodedPassword";
    Role role = Role.USER;
    AuthenticateCmd cmd = new AuthenticateCmd(email, password, role);
    UUID userId = UUID.randomUUID();
    AuthenticatableUser user = new AuthenticatableUser(userId, email, encodedPassword, "Test User", role);

    AuthToken tokenIssuer = new AuthToken("access_token", "refresh_token");

    given(loadAuthenticatableUser.operate(email, role)).willReturn(Optional.of(user));
    given(passwordEncoder.matches(password, encodedPassword)).willReturn(true);
    given(issueTokenUseCase.createToken(any(IssueTokenCmd.class))).willReturn(tokenIssuer);

    // When
    AuthenticatedToken token = authenticateService.operate(cmd);

    // Then
    assertThat(token).isNotNull();
    assertThat(token.accessToken()).isNotBlank();
    assertThat(token.refreshToken()).isNotBlank();
    verify(issueTokenUseCase).createToken(any(IssueTokenCmd.class));
  }

  @Test
  @DisplayName("Scenario: 실패 - 존재하지 않는 사용자로 로그인 시도 시 예외가 발생한다")
  void operate_fail_user_not_found() {
    // Given
    String email = "notfound@example.com";
    Role role = Role.USER;
    AuthenticateCmd cmd = new AuthenticateCmd(email, "password123!", role);
    given(loadAuthenticatableUser.operate(email, role)).willReturn(Optional.empty());

    // When & Then
    org.assertj.core.api.Assertions.assertThatThrownBy(() -> authenticateService.operate(cmd))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.user.not_found");
  }

  @Test
  @DisplayName("Scenario: 실패 - 잘못된 비밀번호로 로그인 시도 시 예외가 발생한다")
  void operate_fail_invalid_password() {
    // Given
    String email = "test@example.com";
    String password = "wrongPassword123!";
    Role role = Role.USER;
    AuthenticateCmd cmd = new AuthenticateCmd(email, password, role);
    AuthenticatableUser user = new AuthenticatableUser(UUID.randomUUID(), email, "encodedPassword", "Name", role);

    given(loadAuthenticatableUser.operate(email, role)).willReturn(Optional.of(user));
    given(passwordEncoder.matches(password, user.password())).willReturn(false);

    // When & Then
    org.assertj.core.api.Assertions.assertThatThrownBy(() -> authenticateService.operate(cmd))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.auth.invalid_password");
  }
}
