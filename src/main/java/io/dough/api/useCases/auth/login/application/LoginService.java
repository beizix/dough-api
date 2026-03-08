package io.dough.api.useCases.auth.login.application;

import io.dough.api.useCases.auth.login.application.model.GetUserResult;
import io.dough.api.useCases.auth.login.domain.LoginCmd;
import io.dough.api.useCases.auth.issueToken.application.IssueTokenUseCase;
import io.dough.api.useCases.shared.domain.auth.AuthToken;
import io.dough.api.useCases.auth.issueToken.domain.CreateTokenCmd;
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
  public AuthToken operate(LoginCmd cmd) {
    GetUserResult user =
        getUser
            .operate(cmd.email(), cmd.role())
            .orElseThrow(() -> new IllegalArgumentException("exception.user.not_found"));

    if (!passwordEncoder.matches(cmd.password(), user.password())) {
      throw new IllegalArgumentException("exception.auth.invalid_password");
    }

    return issueTokenUseCase.createToken(
        new CreateTokenCmd(user.id(), user.email(), user.displayName(), user.role()));
  }
}
