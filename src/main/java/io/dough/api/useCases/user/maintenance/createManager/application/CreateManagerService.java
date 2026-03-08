package io.dough.api.useCases.user.maintenance.createManager.application;

import io.dough.api.useCases.user.maintenance.createManager.domain.CreateManagerCmd;
import io.dough.api.useCases.user.maintenance.createManager.domain.ManagerCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateManagerService implements CreateManagerUseCase {

  private final SaveManager saveManager;
  private final PasswordEncoder passwordEncoder;

  @Override
  public ManagerCreated operate(CreateManagerCmd cmd) {
    CreateManagerCmd encodedCmd =
        new CreateManagerCmd(
            cmd.email(), cmd.displayName(), passwordEncoder.encode(cmd.password()), cmd.role());

    return saveManager.operate(encodedCmd);
  }
}
