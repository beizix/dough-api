package io.dough.api.useCases.auth.issueToken.domain;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.shared.domain.auth.Token;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;

public record TokenIssuer(
    SecretKey key,
    UUID uuid,
    String email,
    String displayName,
    Role role,
    Date now,
    long accessTokenValidity,
    long refreshTokenValidity) {

  public String getAccessToken() {
    return generateToken(Token.access, accessTokenValidity);
  }

  public String getRefreshToken() {
    return generateToken(Token.refresh, refreshTokenValidity);
  }

  private String generateToken(Token type, long validity) {
    var roleStr = role.getAuthority();
    var privileges = role.getPrivileges().stream().map(Enum::name).distinct().toList();
    Date expiration = new Date(now.getTime() + validity);

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
