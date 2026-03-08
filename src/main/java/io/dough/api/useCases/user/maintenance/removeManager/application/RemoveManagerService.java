package io.dough.api.useCases.user.maintenance.removeManager.application;

import io.dough.api.useCases.user.maintenance.removeManager.domain.ManagerRemoved;
import io.dough.api.useCases.user.maintenance.removeManager.domain.RemoveManagerCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RemoveManagerService implements RemoveManagerUseCase {

  private final DeleteManager deleteManager;

  @Override
  public ManagerRemoved operate(RemoveManagerCmd cmd) {
    return deleteManager.operate(cmd);
  }
}
