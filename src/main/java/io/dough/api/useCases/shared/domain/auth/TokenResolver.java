package io.dough.api.useCases.shared.domain.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Collections;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenResolver {

  private final SecretKey key;
  private final String token;
  private Claims cachedClaims;

  public TokenResolver(SecretKey key, String token) {
    this.key = key;
    this.token = token;
  }

  public boolean validate() {
    try {
      getClaims();
      return true;
    } catch (Exception e) {
      log.debug("Token validation failed: {}", e.getMessage());
      return false;
    }
  }

  public String getSubject() {
    return getClaims().getSubject();
  }

  public String getEmail() {
    return getClaims().get("email", String.class);
  }

  public String getDisplayName() {
    return getClaims().get("displayName", String.class);
  }

  public String getRole() {
    return getClaims().get("role", String.class);
  }

  @SuppressWarnings("unchecked")
  public List<String> getPrivileges() {
    List<String> privileges = getClaims().get("privileges", List.class);
    return privileges != null ? privileges : Collections.emptyList();
  }

  public Token getType() {
    String type = getClaims().get("type", String.class);
    return Token.valueOf(type);
  }

  private Claims getClaims() {
    if (cachedClaims == null) {
      cachedClaims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
    return cachedClaims;
  }
}
