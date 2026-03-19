package io.dough.api.useCases.auth.authenticate.application.service;

import io.dough.api.useCases.auth.authenticate.application.port.in.AuthenticateCmd;
import io.dough.api.useCases.auth.authenticate.application.port.in.AuthenticateUseCase;
import io.dough.api.useCases.auth.authenticate.application.port.in.AuthenticatedToken;
import io.dough.api.useCases.auth.authenticate.application.port.out.AuthenticatableUser;
import io.dough.api.useCases.auth.authenticate.application.port.out.LoadAuthenticatableUser;
import io.dough.api.useCases.auth.issueToken.application.port.in.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.port.in.IssueTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.port.in.IssueTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateService implements AuthenticateUseCase {

  private final LoadAuthenticatableUser loadAuthenticatableUser;
  private final IssueTokenUseCase issueTokenUseCase;
  private final PasswordEncoder passwordEncoder;

  @Override
  public AuthenticatedToken operate(AuthenticateCmd cmd) {
    AuthenticatableUser authUser =
        loadAuthenticatableUser
            .operate(cmd.email(), cmd.role())
            .orElseThrow(() -> new IllegalArgumentException("exception.user.not_found"));

    authUser.validatePassword(cmd.password(), passwordEncoder);

    AuthToken authToken =
        issueTokenUseCase.createToken(
            new IssueTokenCmd(
                authUser.id(), authUser.email(), authUser.displayName(), authUser.role()));

    return new AuthenticatedToken(authToken.accessToken(), authToken.refreshToken());
  }
}
