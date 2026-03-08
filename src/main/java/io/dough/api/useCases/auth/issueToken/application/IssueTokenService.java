package io.dough.api.useCases.auth.issueToken.application;

import io.dough.api.useCases.auth.resolveToken.application.ResolveTokenUseCase;
import io.dough.api.useCases.shared.domain.auth.AuthToken;
import io.dough.api.useCases.auth.issueToken.domain.CreateTokenCmd;
import io.dough.api.useCases.auth.issueToken.domain.RefreshTokenCmd;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IssueTokenService implements IssueTokenUseCase {

  private final SecretKey key;
  private final long accessTokenValidity;
  private final long refreshTokenValidity;
  private final HandleTokenRefresh handleTokenRefresh;
  private final ResolveTokenUseCase resolveTokenUseCase;

  public IssueTokenService(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-token-validity}") long accessTokenValidity,
      @Value("${jwt.refresh-token-validity}") long refreshTokenValidity,
      HandleTokenRefresh handleTokenRefresh,
      ResolveTokenUseCase resolveTokenUseCase) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenValidity = accessTokenValidity;
    this.refreshTokenValidity = refreshTokenValidity;
    this.handleTokenRefresh = handleTokenRefresh;
    this.resolveTokenUseCase = resolveTokenUseCase;
  }

  @Override
  public AuthToken createToken(CreateTokenCmd cmd) {
    var role = cmd.role().getAuthority();
    var privileges = cmd.role().getPrivileges().stream().map(Enum::name).distinct().toList();

    String accessToken = createTokenString(
        cmd.uuid().toString(), cmd.email(), cmd.displayName(), role, privileges, accessTokenValidity);
    String refreshToken = createTokenString(
        cmd.uuid().toString(), cmd.email(), cmd.displayName(), role, privileges, refreshTokenValidity);

    handleTokenRefresh.save(cmd.uuid(), refreshToken);

    return new AuthToken(accessToken, refreshToken);
  }

  @Override
  public AuthToken refreshToken(RefreshTokenCmd cmd) {
    if (!resolveTokenUseCase.validateToken(cmd.refreshToken())) {
      throw new IllegalArgumentException("exception.auth.invalid_refresh_token");
    }

    return handleTokenRefresh
        .get(cmd.refreshToken())
        .map(user -> createToken(new CreateTokenCmd(user.uuid(), user.email(), user.displayName(), user.role())))
        .orElseThrow(() -> new IllegalArgumentException("exception.auth.invalid_refresh_token"));
  }

  private String createTokenString(
      String subject, String email, String displayName, String role, List<String> privileges, long validity) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + validity);

    return Jwts.builder()
        .subject(subject)
        .claim("email", email)
        .claim("displayName", displayName)
        .claim("role", role)
        .claim("privileges", privileges)
        .issuedAt(now)
        .expiration(expiration)
        .signWith(key)
        .compact();
  }
}
