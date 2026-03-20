package io.dough.api.useCases.user.mgmt.getUserDetail.application.port.out;

import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.GetUserDetailCmd;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.UserDetailLoaded;

public interface LoadUserDetail {
  UserDetailLoaded operate(GetUserDetailCmd cmd);
}
