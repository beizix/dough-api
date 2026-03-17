package io.dough.api.useCases.auth.issueToken.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.dough.api.useCases.auth.issueToken.application.model.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.model.IssueTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.model.RefreshTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.model.RefreshUserLoaded;
import io.dough.api.useCases.auth.issueToken.domain.TokenIssuer;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IssueTokenServiceTest {

  private IssueTokenService issueTokenService;
  private ManageRefreshToken manageRefreshToken;

  private final String secret = "v-api-test-secret-key-must-be-long-enough-for-hs256";
  private final long accessValidity = 60000L;
  private final long refreshValidity = 120000L;

  @BeforeEach
  void setUp() {
    manageRefreshToken = mock(ManageRefreshToken.class);
    issueTokenService = new IssueTokenService(secret, accessValidity, refreshValidity, manageRefreshToken);
  }

  @Test
  @DisplayName("Scenario: 성공 - 토큰 생성 시 DB에 Refresh Token을 저장한다")
  void create_token_success() {
    // Given
    UUID uuid = UUID.randomUUID();
    IssueTokenCmd cmd = new IssueTokenCmd(uuid, "test@example.com", "Test User", Role.USER);

    // When
    AuthToken token = issueTokenService.createToken(cmd);

    // Then
    assertThat(token).isNotNull();
    verify(manageRefreshToken).save(uuid, token.refreshToken());
  }

  @Test
  @DisplayName("Scenario: 성공 - 리프레시 토큰이 유효하면 재발급한다")
  void refresh_token_success() {
    // Given
    UUID uuid = UUID.randomUUID();
    String email = "test@example.com";
    Role role = Role.USER;

    // 실제로 유효한 토큰 생성
    TokenIssuer issuer = new TokenIssuer(
        Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)),
        uuid, email, "User", role, new Date(), accessValidity, refreshValidity
    );
    String refreshToken = issuer.getRefreshToken();

    when(manageRefreshToken.get(refreshToken))
        .thenReturn(Optional.of(new RefreshUserLoaded(uuid, email, "User", role)));

    // When
    AuthToken newToken = issueTokenService.refreshToken(new RefreshTokenCmd(refreshToken));

    // Then
    assertThat(newToken).isNotNull();
    verify(manageRefreshToken).save(uuid, newToken.refreshToken());
  }
}
