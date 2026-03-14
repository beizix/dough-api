package io.dough.api.useCases.user.maintenance.getUser.application;

import io.dough.api.useCases.user.maintenance.getUser.application.model.GetUserCmd;
import io.dough.api.useCases.user.maintenance.getUser.application.model.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUserDetailService implements GetUserUseCase {

  private final LoadUser loadUser;

  @Override
  public UserDetail operate(GetUserCmd cmd) {
    return loadUser.operate(cmd);
  }
}
