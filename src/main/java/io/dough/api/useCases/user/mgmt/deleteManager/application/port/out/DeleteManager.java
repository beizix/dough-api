package io.dough.api.useCases.user.mgmt.deleteManager.application.port.out;

import io.dough.api.useCases.user.mgmt.deleteManager.application.port.in.DeleteManagerCmd;
import io.dough.api.useCases.user.mgmt.deleteManager.application.port.in.ManagerDeleted;

public interface DeleteManager {
  ManagerDeleted operate(DeleteManagerCmd cmd);
}
