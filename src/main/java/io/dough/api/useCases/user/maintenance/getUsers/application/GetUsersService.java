package io.dough.api.useCases.user.maintenance.getUsers.application;

import io.dough.api.useCases.user.maintenance.getUsers.domain.GetUsers;
import io.dough.api.useCases.user.maintenance.getUsers.domain.GetUsersCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUsersService implements GetUsersUseCase {

  private final LoadUsers loadUsers;

  @Override
  public GetUsers operate(GetUsersCmd cmd) {
    return loadUsers.operate(cmd);
  }
}
