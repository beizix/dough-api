package io.dough.api.useCases.auth.resolveToken.application;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.useCases.auth.issueToken.application.HandleTokenRefresh;
import io.dough.api.useCases.auth.issueToken.application.IssueTokenService;
import io.dough.api.useCases.auth.issueToken.application.model.CreateTokenCmd;
import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ResolveTokenServiceTest {

  private ResolveTokenService resolveTokenService;
  private IssueTokenService issueTokenService; // 토큰 생성을 위해 필요

  private final String secret = "v-api-test-secret-key-must-be-long-enough-for-hs256";

  @BeforeEach
  void setUp() {
    resolveTokenService = new ResolveTokenService(secret);
    // IssueTokenService는 단순 유틸리티성으로 토큰 생성을 위해 사용 (모킹 대신 실제 객체 사용 가능)
    issueTokenService =
        new IssueTokenService(
            secret, 60000L, 120000L, Mockito.mock(HandleTokenRefresh.class), resolveTokenService);
  }

  @Test
  @DisplayName("Scenario: 성공 - 유효한 토큰 검증 및 정보 추출")
  void validate_and_extract_success() {
    // Given
    UUID uuid = UUID.randomUUID();
    String email = "test@example.com";
    CreateTokenCmd cmd = new CreateTokenCmd(uuid, email, "Test User", Role.USER);
    String token = issueTokenService.createToken(cmd).getAccessToken();

    // When & Then
    assertThat(resolveTokenService.validateToken(token)).isTrue();
    assertThat(resolveTokenService.getSubject(token)).isEqualTo(uuid.toString());
    assertThat(resolveTokenService.getEmail(token)).isEqualTo(email);
    assertThat(resolveTokenService.getRole(token)).isEqualTo("ROLE_USER");
  }

  @Test
  @DisplayName("Scenario: 실패 - 변조된 토큰은 검증에 실패한다")
  void validate_fail_invalid_token() {
    // Given
    String invalidToken = "invalid.token.string";

    // When
    boolean isValid = resolveTokenService.validateToken(invalidToken);

    // Then
    assertThat(isValid).isFalse();
  }
}
