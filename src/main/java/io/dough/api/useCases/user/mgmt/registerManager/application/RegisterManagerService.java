package io.dough.api.useCases.user.mgmt.registerManager.application;

import io.dough.api.useCases.user.mgmt.registerManager.application.model.RegisterManagerCmd;
import io.dough.api.useCases.user.mgmt.registerManager.application.model.ManagerRegistered;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterManagerService implements RegisterManagerUseCase {

  private final RecordManager recordManager;
  private final PasswordEncoder passwordEncoder;

  @Override
  public ManagerRegistered operate(RegisterManagerCmd cmd) {
    if (recordManager.existsByEmailAndRole(cmd.email(), cmd.role())) {
      throw new IllegalArgumentException("exception.auth.email_already_exists");
    }

    RegisterManagerCmd encodedCmd =
        new RegisterManagerCmd(
            cmd.email(), cmd.displayName(), passwordEncoder.encode(cmd.password()), cmd.role());

    return recordManager.operate(encodedCmd);
  }
}
