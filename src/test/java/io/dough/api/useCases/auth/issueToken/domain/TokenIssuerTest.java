package io.dough.api.useCases.auth.issueToken.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.shared.domain.auth.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenIssuerTest {

  private SecretKey key;
  private final String secret = "v-api-test-secret-key-must-be-long-enough-for-hs256";
  private final UUID uuid = UUID.randomUUID();
  private final String email = "test@example.com";
  private final String displayName = "Test User";
  private final Role role = Role.USER;
  private final Date now = new Date();
  private final long accessValidity = 60000L; // 1 min
  private final long refreshValidity = 120000L; // 2 min

  @BeforeEach
  void setUp() {
    key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  @DisplayName("Scenario: 성공 - 액세스 토큰 생성 및 클레임 검증")
  void generate_access_token_success() {
    // Given
    TokenIssuer issuer =
        new TokenIssuer(key, uuid, email, displayName, role, now, accessValidity, refreshValidity);

    // When
    String token = issuer.getAccessToken();

    // Then
    Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

    assertThat(claims.getSubject()).isEqualTo(uuid.toString());
    assertThat(claims.get("email")).isEqualTo(email);
    assertThat(claims.get("displayName")).isEqualTo(displayName);
    assertThat(claims.get("role")).isEqualTo(role.getAuthority());
    assertThat(claims.get("type")).isEqualTo(Token.access.name());
    assertThat(claims.getIssuedAt()).isCloseTo(now, 1000L);
    assertThat(claims.getExpiration()).isCloseTo(new Date(now.getTime() + accessValidity), 1000L);
  }

  @Test
  @DisplayName("Scenario: 성공 - 리프레시 토큰 생성 및 클레임 검증")
  void generate_refresh_token_success() {
    // Given
    TokenIssuer issuer =
        new TokenIssuer(key, uuid, email, displayName, role, now, accessValidity, refreshValidity);

    // When
    String token = issuer.getRefreshToken();

    // Then
    Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

    assertThat(claims.getSubject()).isEqualTo(uuid.toString());
    assertThat(claims.get("type")).isEqualTo(Token.refresh.name());
    assertThat(claims.getExpiration()).isCloseTo(new Date(now.getTime() + refreshValidity), 1000L);
  }
}
