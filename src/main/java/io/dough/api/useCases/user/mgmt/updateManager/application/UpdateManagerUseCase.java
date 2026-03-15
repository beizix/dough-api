package io.dough.api.useCases.user.mgmt.updateManager.application;

import io.dough.api.useCases.user.mgmt.updateManager.application.model.ManagerUpdated;
import io.dough.api.useCases.user.mgmt.updateManager.application.model.UpdateManagerCmd;

public interface UpdateManagerUseCase {
  ManagerUpdated operate(UpdateManagerCmd cmd);
}
