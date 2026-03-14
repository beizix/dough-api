package io.dough.api.useCases.user.maintenance.removeManager.application;

import io.dough.api.useCases.user.maintenance.removeManager.application.model.ManagerRemoved;
import io.dough.api.useCases.user.maintenance.removeManager.application.model.RemoveManagerCmd;

public interface DeleteManager {
  ManagerRemoved operate(RemoveManagerCmd cmd);
}
