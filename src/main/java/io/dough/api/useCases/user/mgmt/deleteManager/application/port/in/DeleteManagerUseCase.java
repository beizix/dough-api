package io.dough.api.useCases.user.mgmt.deleteManager.application.port.in;

public interface DeleteManagerUseCase {
  ManagerDeleted operate(DeleteManagerCmd cmd);
}
