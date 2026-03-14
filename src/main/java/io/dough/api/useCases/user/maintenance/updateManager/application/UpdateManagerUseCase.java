package io.dough.api.useCases.user.maintenance.updateManager.application;

import io.dough.api.useCases.user.maintenance.updateManager.application.model.ManagerUpdated;
import io.dough.api.useCases.user.maintenance.updateManager.application.model.UpdateManagerCmd;

public interface UpdateManagerUseCase {
  ManagerUpdated operate(UpdateManagerCmd cmd);
}
