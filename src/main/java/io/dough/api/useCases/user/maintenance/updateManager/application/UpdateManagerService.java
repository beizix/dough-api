package io.dough.api.useCases.user.maintenance.updateManager.application;

import io.dough.api.useCases.user.maintenance.updateManager.domain.ManagerUpdated;
import io.dough.api.useCases.user.maintenance.updateManager.domain.UpdateManagerCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateManagerService implements UpdateManagerUseCase {

  private final UpdateManagerPortOut updateManagerPortOut;
  private final PasswordEncoder passwordEncoder;

  @Override
  public ManagerUpdated operate(UpdateManagerCmd cmd) {
    UpdateManagerCmd encodedCmd =
        new UpdateManagerCmd(
            cmd.email(), cmd.displayName(), passwordEncoder.encode(cmd.password()));

    return updateManagerPortOut.operate(encodedCmd);
  }
}
