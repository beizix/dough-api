package io.dough.api.useCases.user.maintenance.updateManager.application;

import io.dough.api.useCases.user.maintenance.updateManager.domain.ManagerUpdated;
import io.dough.api.useCases.user.maintenance.updateManager.domain.UpdateManagerCmd;

public interface UpdateManagerUseCase {
  ManagerUpdated operate(UpdateManagerCmd cmd);
}
