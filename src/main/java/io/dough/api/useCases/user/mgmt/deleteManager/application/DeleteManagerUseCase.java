package io.dough.api.useCases.user.mgmt.deleteManager.application;

import io.dough.api.useCases.user.mgmt.deleteManager.application.model.DeleteManagerCmd;
import io.dough.api.useCases.user.mgmt.deleteManager.application.model.ManagerDeleted;

public interface DeleteManagerUseCase {
  ManagerDeleted operate(DeleteManagerCmd cmd);
}
