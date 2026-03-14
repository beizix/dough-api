package io.dough.api.useCases.user.maintenance.createManager.application;

import io.dough.api.useCases.user.maintenance.createManager.application.model.CreateManagerCmd;
import io.dough.api.useCases.user.maintenance.createManager.application.model.ManagerCreated;

public interface CreateManagerUseCase {
  ManagerCreated operate(CreateManagerCmd cmd);
}
