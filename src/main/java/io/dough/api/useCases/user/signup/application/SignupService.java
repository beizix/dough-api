package io.dough.api.useCases.user.signup.application;

import io.dough.api.useCases.auth.issueToken.application.IssueTokenUseCase;
import io.dough.api.useCases.shared.domain.auth.AuthToken;
import io.dough.api.useCases.auth.issueToken.domain.CreateTokenCmd;
import io.dough.api.useCases.user.signup.domain.SignupCmd;
import io.dough.api.useCases.user.signup.application.model.SignupUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {

  private final ManageSignup manageSignup;
  private final PasswordEncoder passwordEncoder;
  private final IssueTokenUseCase issueTokenUseCase;

  @Override
  @Transactional
  public AuthToken operate(SignupCmd cmd) {
    if (manageSignup.existsByEmailAndRole(cmd.email(), cmd.role())) {
      throw new IllegalArgumentException("exception.auth.email_already_exists");
    }

    String encodedPassword = passwordEncoder.encode(cmd.password());
    SignupUser user =
        new SignupUser(null, cmd.email(), encodedPassword, cmd.displayName(), cmd.role());

    SignupUser savedUser = manageSignup.save(user);

    return issueTokenUseCase.createToken(
        new CreateTokenCmd(
            savedUser.id(), savedUser.email(), savedUser.displayName(), savedUser.role()));
  }
}
