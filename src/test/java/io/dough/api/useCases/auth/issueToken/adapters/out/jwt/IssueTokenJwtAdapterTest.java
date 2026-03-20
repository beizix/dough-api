package io.dough.api.useCases.auth.issueToken.adapters.out.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.useCases.shared.application.service.auth.TokenResolver;
import io.dough.api.useCases.shared.application.service.auth.TokenType;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IssueTokenJwtAdapterTest {

  private IssueTokenJwtAdapter adapter;
  private final String secret = "v-api-test-secret-key-must-be-long-enough-for-hs256";
  private final long accessValidity = 60000L;
  private final long refreshValidity = 120000L;
  private SecretKey key;

  @BeforeEach
  void setUp() {
    adapter = new IssueTokenJwtAdapter(secret, accessValidity, refreshValidity);
    key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  @DisplayName("Scenario: 성공 - 액세스 토큰 생성 및 검증")
  void get_access_token_success() {
    // Given
    UUID uuid = UUID.randomUUID();
    String email = "test@example.com";
    String displayName = "Test User";
    Role role = Role.USER;

    // When
    String token = adapter.getAccessToken(uuid, email, displayName, role);

    // Then
    assertThat(token).isNotNull();
    TokenResolver resolver = new TokenResolver(key, token);
    assertThat(resolver.validate()).isTrue();
    assertThat(resolver.getSubject()).isEqualTo(uuid.toString());
    assertThat(resolver.getEmail()).isEqualTo(email);
    assertThat(resolver.getType()).isEqualTo(TokenType.access);
  }

  @Test
  @DisplayName("Scenario: 성공 - 리프레시 토큰 생성 및 검증")
  void get_refresh_token_success() {
    // Given
    UUID uuid = UUID.randomUUID();
    String email = "test@example.com";
    String displayName = "Test User";
    Role role = Role.USER;

    // When
    String token = adapter.getRefreshToken(uuid, email, displayName, role);

    // Then
    assertThat(token).isNotNull();
    TokenResolver resolver = new TokenResolver(key, token);
    assertThat(resolver.validate()).isTrue();
    assertThat(resolver.getSubject()).isEqualTo(uuid.toString());
    assertThat(resolver.getType()).isEqualTo(TokenType.refresh);
  }
}
