package io.dough.api.useCases.user.mgmt.getUserDetail.application.service;

import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.GetUserDetailCmd;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.GetUserDetailUseCase;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.UserDetailLoaded;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.out.LoadUserDetail;
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
