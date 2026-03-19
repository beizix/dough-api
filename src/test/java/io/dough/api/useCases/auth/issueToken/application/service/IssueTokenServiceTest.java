package io.dough.api.useCases.auth.issueToken.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.dough.api.useCases.auth.issueToken.application.port.in.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.port.in.IssueTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.port.in.RefreshTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.port.out.ManageRefreshToken;
import io.dough.api.useCases.auth.issueToken.application.port.out.RefreshUserLoaded;
import io.dough.api.useCases.auth.issueToken.application.port.out.TokenProvider;
import io.dough.api.useCases.auth.issueToken.application.service.IssueTokenService;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.shared.domain.auth.Token;
import io.dough.api.useCases.shared.domain.auth.TokenResolver;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class IssueTokenServiceTest {

  private IssueTokenService issueTokenService;
  private TokenProvider tokenProvider;
  private ManageRefreshToken manageRefreshToken;

  private final String secret = "v-api-test-secret-key-must-be-long-enough-for-hs256";

  @BeforeEach
  void setUp() {
    tokenProvider = mock(TokenProvider.class);
    manageRefreshToken = mock(ManageRefreshToken.class);
    issueTokenService = new IssueTokenService(tokenProvider, manageRefreshToken);
    ReflectionTestUtils.setField(issueTokenService, "secret", secret);
  }

  @Test
  @DisplayName("Scenario: 성공 - 토큰 생성 시 DB에 Refresh Token을 저장한다")
  void create_token_success() {
    // Given
    UUID uuid = UUID.randomUUID();
    IssueTokenCmd cmd = new IssueTokenCmd(uuid, "test@example.com", "Test User", Role.USER);
    String mockAccessToken = "access-token";
    String mockRefreshToken = "refresh-token";

    when(tokenProvider.getAccessToken(any(), any(), any(), any())).thenReturn(mockAccessToken);
    when(tokenProvider.getRefreshToken(any(), any(), any(), any())).thenReturn(mockRefreshToken);

    // When
    AuthToken token = issueTokenService.createToken(cmd);

    // Then
    assertThat(token).isNotNull();
    assertThat(token.accessToken()).isEqualTo(mockAccessToken);
    assertThat(token.refreshToken()).isEqualTo(mockRefreshToken);
    verify(manageRefreshToken).updateRefreshToken(uuid, mockRefreshToken);
  }

  @Test
  @DisplayName("Scenario: 성공 - 리프레시 토큰이 유효하면 재발급한다")
  void refresh_token_success() {
    // Given
    UUID uuid = UUID.randomUUID();
    String email = "test@example.com";
    Role role = Role.USER;

    // 실제로 유효한 토큰 생성 (테스트용)
    String refreshToken =
        Jwts.builder()
            .subject(uuid.toString())
            .claim("type", Token.refresh.name())
            .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
            .compact();

    when(manageRefreshToken.loadRefreshUser(refreshToken))
        .thenReturn(Optional.of(new RefreshUserLoaded(uuid, email, "User", role)));
    when(tokenProvider.getAccessToken(any(), any(), any(), any())).thenReturn("new-access-token");
    when(tokenProvider.getRefreshToken(any(), any(), any(), any())).thenReturn("new-refresh-token");

    // When
    AuthToken newToken = issueTokenService.refreshToken(new RefreshTokenCmd(refreshToken));

    // Then
    assertThat(newToken).isNotNull();
    verify(manageRefreshToken).updateRefreshToken(uuid, newToken.refreshToken());
  }
}
