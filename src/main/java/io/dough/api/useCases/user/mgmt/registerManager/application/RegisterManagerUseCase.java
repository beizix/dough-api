package io.dough.api.useCases.user.mgmt.registerManager.application;

import io.dough.api.useCases.user.mgmt.registerManager.application.model.RegisterManagerCmd;
import io.dough.api.useCases.user.mgmt.registerManager.application.model.ManagerRegistered;

public interface RegisterManagerUseCase {
  ManagerRegistered operate(RegisterManagerCmd cmd);
}
