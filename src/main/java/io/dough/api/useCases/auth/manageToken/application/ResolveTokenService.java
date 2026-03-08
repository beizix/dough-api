package io.dough.api.useCases.auth.manageToken.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResolveTokenService implements ResolveTokenUseCase {

  private final SecretKey key;

  public ResolveTokenService(@Value("${jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public boolean validateToken(String token) {
    try {
      parseClaims(token);
      return true;
    } catch (Exception e) {
      log.error("Token validation failed: {}", e.getMessage());
      return false;
    }
  }

  @Override
  public String getSubject(String token) {
    return parseClaims(token).getSubject();
  }

  @Override
  public String getDisplayName(String token) {
    return parseClaims(token).get("displayName", String.class);
  }

  @Override
  public String getEmail(String token) {
    return parseClaims(token).get("email", String.class);
  }

  @Override
  public String getRole(String token) {
    try {
      return parseClaims(token).get("role", String.class);
    } catch (Exception e) {
      log.error("Failed to extract role from token", e);
      return null;
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<String> getPrivileges(String token) {
    try {
      return parseClaims(token).get("privileges", List.class);
    } catch (Exception e) {
      log.error("Failed to extract privileges from token", e);
      return Collections.emptyList();
    }
  }

  private Claims parseClaims(String token) {
    try {
      return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    } catch (Exception e) {
      throw new IllegalArgumentException("exception.auth.invalid_token", e);
    }
  }
}
