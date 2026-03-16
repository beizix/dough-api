package io.dough.api.useCases.auth.issueToken.application;

import io.dough.api.useCases.auth.issueToken.application.model.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.model.IssueTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.model.RefreshTokenCmd;
import io.dough.api.useCases.auth.issueToken.domain.TokenIssuer;
import io.dough.api.useCases.shared.domain.auth.Token;
import io.dough.api.useCases.shared.domain.auth.TokenResolver;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IssueTokenService implements IssueTokenUseCase {

  private final SecretKey key;
  private final long accessTokenValidity;
  private final long refreshTokenValidity;
  private final ManageRefreshToken manageRefreshToken;

  public IssueTokenService(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-token-validity}") long accessTokenValidity,
      @Value("${jwt.refresh-token-validity}") long refreshTokenValidity,
      ManageRefreshToken manageRefreshToken) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenValidity = accessTokenValidity;
    this.refreshTokenValidity = refreshTokenValidity;
    this.manageRefreshToken = manageRefreshToken;
  }

  @Override
  public AuthToken createToken(IssueTokenCmd cmd) {
    TokenIssuer tokenIssuer = new TokenIssuer(
        key,
        cmd.uuid(),
        cmd.email(),
        cmd.displayName(),
        cmd.role(),
        new Date(),
        accessTokenValidity,
        refreshTokenValidity);

    manageRefreshToken.save(cmd.uuid(), tokenIssuer.getRefreshToken());

    return new AuthToken(tokenIssuer.getAccessToken(), tokenIssuer.getRefreshToken());
  }

  @Override
  public AuthToken refreshToken(RefreshTokenCmd cmd) {
    TokenResolver tokenResolver = new TokenResolver(key, cmd.refreshToken());
    if (!tokenResolver.validate() || tokenResolver.getType() != Token.refresh) {
      throw new IllegalArgumentException("exception.auth.invalid_refresh_token");
    }

    return manageRefreshToken
        .get(cmd.refreshToken())
        .map(
            user -> createToken(
                new IssueTokenCmd(user.uuid(), user.email(), user.displayName(), user.role())))
        .orElseThrow(() -> new IllegalArgumentException("exception.auth.invalid_refresh_token"));
  }
}
