package io.dough.api.useCases.user.mgmt.registerManager.application.port.in;

import io.dough.api.useCases.user.mgmt.registerManager.application.port.in.ManagerRegistered;
import io.dough.api.useCases.user.mgmt.registerManager.application.port.in.RegisterManagerCmd;

public interface RegisterManagerUseCase {
  ManagerRegistered operate(RegisterManagerCmd cmd);
}
