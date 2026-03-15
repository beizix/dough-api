package io.dough.api.useCases.user.mgmt.getUserDetail.application;

import io.dough.api.useCases.user.mgmt.getUserDetail.application.model.GetUserDetailCmd;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.model.UserDetailLoaded;

public interface GetUserDetailUseCase {
  UserDetailLoaded operate(GetUserDetailCmd cmd);
}
