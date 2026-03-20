package io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in;

import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.GetUserDetailCmd;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.UserDetailLoaded;

public interface GetUserDetailUseCase {
  UserDetailLoaded operate(GetUserDetailCmd cmd);
}
