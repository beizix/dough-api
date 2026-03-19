package io.dough.api.useCases.shared.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenResolverTest {

  private SecretKey key;
  private final String secret = "v-api-test-secret-key-must-be-long-enough-for-hs256";
  private final UUID uuid = UUID.randomUUID();
  private final String email = "test@example.com";
  private final String displayName = "Test User";
  private final Role role = Role.USER;
  private final Date now = new Date();
  private final long accessValidity = 60000L;
  private final long refreshValidity = 120000L;

  @BeforeEach
  void setUp() {
    key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  @DisplayName("Scenario: 성공 - 유효한 액세스 토큰 해석")
  void resolve_access_token_success() {
    // Given
    String token =
        Jwts.builder()
            .subject(uuid.toString())
            .claim("email", email)
            .claim("displayName", displayName)
            .claim("type", Token.access.name())
            .claim("role", role.getAuthority())
            .claim("privileges", role.getPrivileges().stream().map(Enum::name).toList())
            .issuedAt(now)
            .expiration(new Date(now.getTime() + accessValidity))
            .signWith(key)
            .compact();

    // When
    TokenResolver resolver = new TokenResolver(key, token);

    // Then
    assertThat(resolver.validate()).isTrue();
    assertThat(resolver.getSubject()).isEqualTo(uuid.toString());
    assertThat(resolver.getEmail()).isEqualTo(email);
    assertThat(resolver.getDisplayName()).isEqualTo(displayName);
    assertThat(resolver.getRole()).isEqualTo(role.getAuthority());
    assertThat(resolver.getType()).isEqualTo(Token.access);
  }

  @Test
  @DisplayName("Scenario: 성공 - 유효한 리프레시 토큰 해석")
  void resolve_refresh_token_success() {
    // Given
    String token =
        Jwts.builder()
            .subject(uuid.toString())
            .claim("type", Token.refresh.name())
            .issuedAt(now)
            .expiration(new Date(now.getTime() + refreshValidity))
            .signWith(key)
            .compact();

    // When
    TokenResolver resolver = new TokenResolver(key, token);

    // Then
    assertThat(resolver.validate()).isTrue();
    assertThat(resolver.getSubject()).isEqualTo(uuid.toString());
    assertThat(resolver.getType()).isEqualTo(Token.refresh);
  }

  @Test
  @DisplayName("Scenario: 실패 - 위조된 토큰 검증 실패")
  void resolve_manipulated_token_fail() {
    // Given
    String token =
        Jwts.builder()
            .subject(uuid.toString())
            .claim("type", Token.access.name())
            .signWith(key)
            .compact()
            + "extra";

    // When
    TokenResolver resolver = new TokenResolver(key, token);

    // Then
    assertThat(resolver.validate()).isFalse();
  }

  @Test
  @DisplayName("Scenario: 실패 - 다른 키로 서명된 토큰 검증 실패")
  void resolve_different_key_token_fail() {
    // Given
    SecretKey anotherKey =
        Keys.hmacShaKeyFor(
            "another-secret-key-very-long-and-long-enough-for-hs256"
                .getBytes(StandardCharsets.UTF_8));
    String token =
        Jwts.builder()
            .subject(uuid.toString())
            .claim("type", Token.access.name())
            .signWith(anotherKey)
            .compact();

    // When
    TokenResolver resolver = new TokenResolver(key, token);

    // Then
    assertThat(resolver.validate()).isFalse();
  }
}
