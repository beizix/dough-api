package io.dough.api.useCases.user.mgmt.deleteManager.application.port.in;

import io.dough.api.useCases.user.mgmt.deleteManager.application.port.in.DeleteManagerCmd;
import io.dough.api.useCases.user.mgmt.deleteManager.application.port.in.ManagerDeleted;

public interface DeleteManagerUseCase {
  ManagerDeleted operate(DeleteManagerCmd cmd);
}
