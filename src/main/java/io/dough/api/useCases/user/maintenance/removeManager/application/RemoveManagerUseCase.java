package io.dough.api.useCases.user.maintenance.removeManager.application;

import io.dough.api.useCases.user.maintenance.removeManager.domain.ManagerRemoved;
import io.dough.api.useCases.user.maintenance.removeManager.domain.RemoveManagerCmd;

public interface RemoveManagerUseCase {
  ManagerRemoved operate(RemoveManagerCmd cmd);
}
