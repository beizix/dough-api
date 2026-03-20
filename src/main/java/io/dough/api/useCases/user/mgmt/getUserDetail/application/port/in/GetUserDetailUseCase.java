package io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in;

public interface GetUserDetailUseCase {
  UserDetailLoaded operate(GetUserDetailCmd cmd);
}
