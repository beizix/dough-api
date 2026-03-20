package io.dough.api.useCases.user.mgmt.updateManager.application.service;

import io.dough.api.useCases.user.mgmt.updateManager.application.port.in.ManagerUpdated;
import io.dough.api.useCases.user.mgmt.updateManager.application.port.in.UpdateManagerCmd;
import io.dough.api.useCases.user.mgmt.updateManager.application.port.in.UpdateManagerUseCase;
import io.dough.api.useCases.user.mgmt.updateManager.application.port.out.UpdateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateManagerService implements UpdateManagerUseCase {

  private final UpdateManager updateManager;
  private final PasswordEncoder passwordEncoder;

  @Override
  public ManagerUpdated operate(UpdateManagerCmd cmd) {
    UpdateManagerCmd encodedCmd =
        new UpdateManagerCmd(
            cmd.email(), cmd.displayName(), passwordEncoder.encode(cmd.password()));

    return updateManager.operate(encodedCmd);
  }
}
