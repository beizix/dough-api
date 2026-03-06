package io.dough.api.useCases.auth.signup.application;

import io.dough.api.useCases.auth.manageToken.application.ManageAuthTokenUseCase;
import io.dough.api.useCases.auth.manageToken.domain.AuthToken;
import io.dough.api.useCases.auth.manageToken.domain.CreateTokenCmd;
import io.dough.api.useCases.auth.signup.domain.SignupCmd;
import io.dough.api.useCases.auth.signup.application.model.SignupUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {

  private final ManageSignup manageSignup;
  private final PasswordEncoder passwordEncoder;
  private final ManageAuthTokenUseCase manageAuthTokenUseCase;

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

    return manageAuthTokenUseCase.createToken(
        new CreateTokenCmd(
            savedUser.id(), savedUser.email(), savedUser.displayName(), savedUser.role()));
  }
}
