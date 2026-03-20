package io.dough.api.useCases.user.mgmt.deleteManager.application.service;

import io.dough.api.useCases.user.mgmt.deleteManager.application.port.in.DeleteManagerCmd;
import io.dough.api.useCases.user.mgmt.deleteManager.application.port.in.DeleteManagerUseCase;
import io.dough.api.useCases.user.mgmt.deleteManager.application.port.in.ManagerDeleted;
import io.dough.api.useCases.user.mgmt.deleteManager.application.port.out.DeleteManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteManagerService implements DeleteManagerUseCase {

  private final DeleteManager deleteManager;

  @Override
  public ManagerDeleted operate(DeleteManagerCmd cmd) {
    return deleteManager.operate(cmd);
  }
}
