package io.dough.api.useCases.auth.resolveToken.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.dough.api.useCases.auth.issueToken.application.IssueTokenService;
import io.dough.api.useCases.auth.issueToken.application.RefreshAuthToken;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.shared.domain.auth.AuthToken;
import io.dough.api.useCases.auth.issueToken.domain.CreateTokenCmd;
import io.dough.api.useCases.auth.issueToken.domain.RefreshTokenCmd;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IssueTokenServiceTest {

  private IssueTokenService issueTokenService;
  private RefreshAuthToken refreshAuthToken;
  private ResolveTokenUseCase resolveTokenUseCase;

  private final String secret = "v-api-test-secret-key-must-be-long-enough-for-hs256";
  private final long accessValidity = 60000L;
  private final long refreshValidity = 120000L;

  @BeforeEach
  void setUp() {
    refreshAuthToken = mock(RefreshAuthToken.class);
    resolveTokenUseCase = mock(ResolveTokenUseCase.class);
    issueTokenService = new IssueTokenService(secret, accessValidity, refreshValidity, refreshAuthToken, resolveTokenUseCase);
  }

  @Test
  @DisplayName("Scenario: 성공 - 토큰 생성 시 DB에 Refresh Token을 저장한다")
  void create_token_success() {
    // Given
    UUID uuid = UUID.randomUUID();
    CreateTokenCmd cmd = new CreateTokenCmd(uuid, "test@example.com", "Test User", Role.USER);

    // When
    AuthToken token = issueTokenService.createToken(cmd);

    // Then
    assertThat(token).isNotNull();
    verify(refreshAuthToken).save(uuid, token.refreshToken());
  }

  @Test
  @DisplayName("Scenario: 성공 - 리프레시 토큰이 유효하면 재발급한다")
  void refresh_token_success() {
    // Given
    String refreshToken = "valid_refresh_token";
    UUID uuid = UUID.randomUUID();
    String email = "test@example.com";
    Role role = Role.USER;

    when(resolveTokenUseCase.validateToken(refreshToken)).thenReturn(true);
    when(refreshAuthToken.get(refreshToken))
        .thenReturn(Optional.of(new RefreshAuthToken.RefreshUser(uuid, email, "User", role)));

    // When
    AuthToken newToken = issueTokenService.refreshToken(new RefreshTokenCmd(refreshToken));

    // Then
    assertThat(newToken).isNotNull();
    verify(refreshAuthToken).save(uuid, newToken.refreshToken());
  }
}
