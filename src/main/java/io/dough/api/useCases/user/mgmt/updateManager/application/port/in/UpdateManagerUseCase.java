package io.dough.api.useCases.user.mgmt.updateManager.application.port.in;

public interface UpdateManagerUseCase {
  ManagerUpdated operate(UpdateManagerCmd cmd);
}
