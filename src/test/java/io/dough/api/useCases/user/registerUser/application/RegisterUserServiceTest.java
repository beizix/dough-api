package io.dough.api.useCases.user.registerUser.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.auth.issueToken.application.port.in.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.port.in.IssueTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.port.in.IssueTokenUseCase;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.registerUser.application.model.RegisterUserCmd;
import io.dough.api.useCases.user.registerUser.application.model.RegisteredToken;
import io.dough.api.useCases.user.registerUser.application.model.RegisteredUser;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

  @InjectMocks private RegisterUserService registerUserService;

  @Mock private RegisterUser registerUser;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private IssueTokenUseCase issueTokenUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 정상적인 회원가입 요청 시 사용자를 저장하고 토큰을 발급한다")
  void signup_success() {
    // Given
    RegisterUserCmd cmd = new RegisterUserCmd("test@dough.io", "rawPassword123!", "Test User");
    UUID userId = UUID.randomUUID();
    RegisteredUser savedUser =
        new RegisteredUser(userId, cmd.email(), "encodedPassword", cmd.displayName(), Role.USER);

    AuthToken tokenIssuer = new AuthToken("access_token", "refresh_token");

    given(registerUser.existsByEmailAndRole(cmd.email(), cmd.role())).willReturn(false);
    given(passwordEncoder.encode(cmd.password())).willReturn("encodedPassword");
    given(registerUser.save(cmd.email(), "encodedPassword", cmd.displayName(), cmd.role()))
        .willReturn(savedUser);
    given(issueTokenUseCase.createToken(any(IssueTokenCmd.class))).willReturn(tokenIssuer);

    // When
    RegisteredToken token = registerUserService.operate(cmd);

    // Then
    assertThat(token).isNotNull();
    assertThat(token.accessToken()).isNotBlank();
    assertThat(token.refreshToken()).isNotBlank();

    verify(registerUser).save(cmd.email(), "encodedPassword", cmd.displayName(), cmd.role());
    verify(issueTokenUseCase)
        .createToken(
            argThat(
                authCmd ->
                    authCmd.uuid().equals(userId)
                        && authCmd.email().equals(cmd.email())
                        && authCmd.role() == cmd.role()));
  }

  @Test
  @DisplayName("Scenario: 실패 - 이미 해당 권한으로 가입된 이메일로 가입 시도 시 예외가 발생한다")
  void signup_fail_duplicate_email() {
    // Given
    RegisterUserCmd cmd = new RegisterUserCmd("duplicate@dough.io", "password123!", "User");
    given(registerUser.existsByEmailAndRole(cmd.email(), cmd.role())).willReturn(true);

    // When & Then
    assertThatThrownBy(() -> registerUserService.operate(cmd))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.auth.email_already_exists");
  }
}
