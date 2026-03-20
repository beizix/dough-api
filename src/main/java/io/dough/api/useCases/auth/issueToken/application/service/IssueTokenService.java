package io.dough.api.useCases.auth.issueToken.application.service;

import io.dough.api.useCases.auth.issueToken.application.port.in.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.port.in.IssueTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.port.in.IssueTokenUseCase;
import io.dough.api.useCases.auth.issueToken.application.port.in.RefreshTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.port.out.ManageRefreshToken;
import io.dough.api.useCases.auth.issueToken.application.port.out.TokenProvider;
import io.dough.api.useCases.shared.application.service.auth.TokenResolver;
import io.dough.api.useCases.shared.application.service.auth.TokenType;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueTokenService implements IssueTokenUseCase {

  private final TokenProvider tokenProvider;
  private final ManageRefreshToken manageRefreshToken;

  @Value("${jwt.secret}")
  private String secret;

  @Override
  public AuthToken createToken(IssueTokenCmd cmd) {
    String accessToken =
        tokenProvider.getAccessToken(cmd.uuid(), cmd.email(), cmd.displayName(), cmd.role());
    String refreshToken =
        tokenProvider.getRefreshToken(cmd.uuid(), cmd.email(), cmd.displayName(), cmd.role());

    manageRefreshToken.updateRefreshToken(cmd.uuid(), refreshToken);

    return new AuthToken(accessToken, refreshToken);
  }

  @Override
  public AuthToken refreshToken(RefreshTokenCmd cmd) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    TokenResolver tokenResolver = new TokenResolver(key, cmd.refreshToken());
    if (!tokenResolver.validate() || tokenResolver.getType() != TokenType.refresh) {
      throw new IllegalArgumentException("exception.auth.invalid_refresh_token");
    }

    return manageRefreshToken
        .loadRefreshUser(cmd.refreshToken())
        .map(
            user ->
                createToken(
                    new IssueTokenCmd(user.uuid(), user.email(), user.displayName(), user.role())))
        .orElseThrow(() -> new IllegalArgumentException("exception.auth.invalid_refresh_token"));
  }
}
