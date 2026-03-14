package io.dough.api.useCases.user.signup.application;

import io.dough.api.useCases.auth.issueToken.application.IssueTokenUseCase;
import io.dough.api.useCases.auth.issueToken.application.model.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.model.IssueTokenCmd;
import io.dough.api.useCases.user.signup.application.model.SignupCmd;
import io.dough.api.useCases.user.signup.application.model.SignupToken;
import io.dough.api.useCases.user.signup.application.model.SignupUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {

  private final RegisterUser registerUser;
  private final PasswordEncoder passwordEncoder;
  private final IssueTokenUseCase issueTokenUseCase;

  @Override
  @Transactional
  public SignupToken operate(SignupCmd cmd) {
    if (registerUser.existsByEmailAndRole(cmd.email(), cmd.role())) {
      throw new IllegalArgumentException("exception.auth.email_already_exists");
    }

    SignupUser savedUser =
        registerUser.save(
            cmd.email(), passwordEncoder.encode(cmd.password()), cmd.displayName(), cmd.role());

    AuthToken authToken =
        issueTokenUseCase.createToken(
            new IssueTokenCmd(
                savedUser.id(), savedUser.email(), savedUser.displayName(), savedUser.role()));

    return new SignupToken(authToken.accessToken(), authToken.refreshToken());
  }
}
