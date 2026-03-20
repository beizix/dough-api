package io.dough.api.useCases.user.registerUser.application.service;

import io.dough.api.useCases.auth.issueToken.application.port.in.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.port.in.IssueTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.port.in.IssueTokenUseCase;
import io.dough.api.useCases.user.registerUser.application.port.in.RegisterUserCmd;
import io.dough.api.useCases.user.registerUser.application.port.in.RegisterUserUseCase;
import io.dough.api.useCases.user.registerUser.application.port.in.RegisteredToken;
import io.dough.api.useCases.user.registerUser.application.port.in.RegisteredUser;
import io.dough.api.useCases.user.registerUser.application.port.out.RegisterUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class RegisterUserService implements RegisterUserUseCase {

  private final RegisterUser registerUser;
  private final PasswordEncoder passwordEncoder;
  private final IssueTokenUseCase issueTokenUseCase;

  @Override
  @Transactional
  public RegisteredToken operate(RegisterUserCmd cmd) {
    if (registerUser.existsByEmailAndRole(cmd.email(), cmd.role())) {
      throw new IllegalArgumentException("exception.auth.email_already_exists");
    }

    RegisteredUser savedUser =
        registerUser.save(
            cmd.email(), passwordEncoder.encode(cmd.password()), cmd.displayName(), cmd.role());

    AuthToken authToken =
        issueTokenUseCase.createToken(
            new IssueTokenCmd(
                savedUser.id(), savedUser.email(), savedUser.displayName(), savedUser.role()));

    return new RegisteredToken(authToken.accessToken(), authToken.refreshToken());
  }
}
