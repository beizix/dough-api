package io.dough.api.useCases.user.mgmt.updateManager.application.port.in;

import io.dough.api.useCases.user.mgmt.updateManager.application.port.in.ManagerUpdated;
import io.dough.api.useCases.user.mgmt.updateManager.application.port.in.UpdateManagerCmd;

public interface UpdateManagerUseCase {
  ManagerUpdated operate(UpdateManagerCmd cmd);
}
