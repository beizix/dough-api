package io.dough.api.useCases.user.maintenance.removeManager.application;

import io.dough.api.useCases.user.maintenance.removeManager.domain.ManagerRemoved;
import io.dough.api.useCases.user.maintenance.removeManager.domain.RemoveManagerCmd;

public interface DeleteManager {
  ManagerRemoved operate(RemoveManagerCmd cmd);
}
