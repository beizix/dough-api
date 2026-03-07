package io.dough.api.useCases.user.maintenance.getUser.application;

import io.dough.api.useCases.user.maintenance.getUser.domain.GetUserCmd;
import io.dough.api.useCases.user.maintenance.getUser.domain.UserDetail;

public interface GetUserUseCase {
  UserDetail operate(GetUserCmd cmd);
}
