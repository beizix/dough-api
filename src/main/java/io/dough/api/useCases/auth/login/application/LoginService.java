package io.dough.api.useCases.auth.login.application;

import io.dough.api.useCases.auth.issueToken.application.IssueTokenUseCase;
import io.dough.api.useCases.auth.issueToken.application.model.CreateTokenCmd;
import io.dough.api.useCases.auth.issueToken.domain.AuthToken;
import io.dough.api.useCases.auth.login.application.model.LoginToken;
import io.dough.api.useCases.auth.login.application.model.LoginCmd;
import io.dough.api.useCases.auth.login.domain.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

  private final GetUser getUser;
  private final IssueTokenUseCase issueTokenUseCase;
  private final PasswordEncoder passwordEncoder;

  @Override
  public LoginToken operate(LoginCmd cmd) {
    LoginUser user =
      getUser
        .operate(cmd.email(), cmd.role())
        .orElseThrow(() -> new IllegalArgumentException("exception.user.not_found"));

    user.validatePassword(cmd.password(), passwordEncoder);

    AuthToken authToken = issueTokenUseCase.createToken(
      new CreateTokenCmd(user.id(), user.email(), user.displayName(), user.role()));

    return new LoginToken(authToken.getAccessToken(), authToken.getRefreshToken());
  }
}
