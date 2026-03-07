package io.dough.api.useCases.user.maintenance.getUsers.application;

import io.dough.api.useCases.user.maintenance.getUsers.domain.GetUsers;
import io.dough.api.useCases.user.maintenance.getUsers.domain.GetUsersCmd;

public interface LoadUsers {
  GetUsers operate(GetUsersCmd cmd);
}
