package io.dough.api.useCases.user.mgmt.registerManager.application;

import io.dough.api.useCases.user.mgmt.registerManager.application.model.ManagerRegistered;
import io.dough.api.useCases.user.mgmt.registerManager.application.model.RegisterManagerCmd;

public interface RegisterManagerUseCase {
  ManagerRegistered operate(RegisterManagerCmd cmd);
}
