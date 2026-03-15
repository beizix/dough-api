package io.dough.api.useCases.user.mgmt.deleteManager.application;

import io.dough.api.useCases.user.mgmt.deleteManager.application.model.DeleteManagerCmd;
import io.dough.api.useCases.user.mgmt.deleteManager.application.model.ManagerDeleted;
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
