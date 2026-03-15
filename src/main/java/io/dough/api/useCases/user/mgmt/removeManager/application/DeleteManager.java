package io.dough.api.useCases.user.mgmt.removeManager.application;

import io.dough.api.useCases.user.mgmt.removeManager.application.model.ManagerRemoved;
import io.dough.api.useCases.user.mgmt.removeManager.application.model.RemoveManagerCmd;

public interface DeleteManager {
  ManagerRemoved operate(RemoveManagerCmd cmd);
}
