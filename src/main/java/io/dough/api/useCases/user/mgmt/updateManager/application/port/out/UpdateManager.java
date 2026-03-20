package io.dough.api.useCases.user.mgmt.updateManager.application.port.out;

import io.dough.api.useCases.user.mgmt.updateManager.application.port.in.ManagerUpdated;
import io.dough.api.useCases.user.mgmt.updateManager.application.port.in.UpdateManagerCmd;

public interface UpdateManager {
  ManagerUpdated operate(UpdateManagerCmd cmd);
}
