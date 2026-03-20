package io.dough.api.useCases.user.mgmt.registerManager.application.port.in;

public interface RegisterManagerUseCase {
  ManagerRegistered operate(RegisterManagerCmd cmd);
}
