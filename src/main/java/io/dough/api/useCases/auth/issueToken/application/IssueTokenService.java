package io.dough.api.useCases.auth.issueToken.application;

import io.dough.api.useCases.auth.issueToken.application.model.CreateTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.model.RefreshTokenCmd;
import io.dough.api.useCases.auth.issueToken.domain.AuthToken;
import io.dough.api.useCases.auth.resolveToken.application.ResolveTokenUseCase;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

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
    AuthToken authToken = new AuthToken(key, cmd.uuid(), cmd.email(), cmd.displayName(), cmd.role(), new Date(), accessTokenValidity, refreshTokenValidity);
    handleTokenRefresh.save(cmd.uuid(), authToken.getRefreshToken());

    return authToken;
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
}
