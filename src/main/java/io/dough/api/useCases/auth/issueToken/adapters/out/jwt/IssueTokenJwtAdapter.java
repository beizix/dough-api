package io.dough.api.useCases.auth.issueToken.adapters.out.jwt;

import io.dough.api.useCases.auth.issueToken.application.port.out.TokenProvider;
import io.dough.api.useCases.shared.application.service.auth.TokenType;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** JWT 기술을 사용하여 토큰을 생성하는 어댑터입니다. */
@Component
public class IssueTokenJwtAdapter implements TokenProvider {

  private final SecretKey key;
  private final long accessTokenValidity;
  private final long refreshTokenValidity;

  public IssueTokenJwtAdapter(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-token-validity}") long accessTokenValidity,
      @Value("${jwt.refresh-token-validity}") long refreshTokenValidity) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenValidity = accessTokenValidity;
    this.refreshTokenValidity = refreshTokenValidity;
  }

  @Override
  public String getAccessToken(UUID uuid, String email, String displayName, Role role) {
    return generateToken(uuid, email, displayName, role, TokenType.access, accessTokenValidity);
  }

  @Override
  public String getRefreshToken(UUID uuid, String email, String displayName, Role role) {
    return generateToken(uuid, email, displayName, role, TokenType.refresh, refreshTokenValidity);
  }

  private String generateToken(
      UUID uuid, String email, String displayName, Role role, TokenType type, long validity) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + validity);
    var roleStr = role.getAuthority();
    var privileges = role.getPrivileges().stream().map(Enum::name).distinct().toList();

    return Jwts.builder()
        .subject(uuid.toString())
        .claim("email", email)
        .claim("displayName", displayName)
        .claim("type", type.name())
        .claim("role", roleStr)
        .claim("privileges", privileges)
        .issuedAt(now)
        .expiration(expiration)
        .signWith(key)
        .compact();
  }
}
