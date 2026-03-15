package io.dough.api.useCases.user.mgmt.getUserDetail.application;

import io.dough.api.useCases.user.mgmt.getUserDetail.application.model.GetUserDetailCmd;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.model.UserDetailLoaded;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUserDetailService implements GetUserDetailUseCase {

  private final LoadUserDetail loadUserDetail;

  @Override
  public UserDetailLoaded operate(GetUserDetailCmd cmd) {
    return loadUserDetail.operate(cmd);
  }
}
