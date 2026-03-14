package io.dough.api.useCases.user.maintenance.getUsers.application;

import io.dough.api.useCases.user.maintenance.getUsers.application.model.GetUsers;
import io.dough.api.useCases.user.maintenance.getUsers.application.model.GetUsersCmd;

public interface GetUsersUseCase {
  GetUsers operate(GetUsersCmd cmd);
}
