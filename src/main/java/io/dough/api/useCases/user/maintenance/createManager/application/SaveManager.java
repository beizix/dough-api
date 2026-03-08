package io.dough.api.useCases.user.maintenance.createManager.application;

import io.dough.api.useCases.user.maintenance.createManager.domain.CreateManagerCmd;
import io.dough.api.useCases.user.maintenance.createManager.domain.ManagerCreated;

public interface SaveManager {
  ManagerCreated operate(CreateManagerCmd cmd);
}
