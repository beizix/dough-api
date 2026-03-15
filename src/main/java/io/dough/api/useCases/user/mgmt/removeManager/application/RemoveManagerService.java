package io.dough.api.useCases.user.mgmt.removeManager.application;

import io.dough.api.useCases.user.mgmt.removeManager.application.model.ManagerRemoved;
import io.dough.api.useCases.user.mgmt.removeManager.application.model.RemoveManagerCmd;
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
