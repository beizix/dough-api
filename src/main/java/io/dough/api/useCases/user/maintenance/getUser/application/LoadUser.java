package io.dough.api.useCases.user.maintenance.getUser.application;

import io.dough.api.useCases.user.maintenance.getUser.application.model.GetUserCmd;
import io.dough.api.useCases.user.maintenance.getUser.application.model.UserDetail;

public interface LoadUser {
  UserDetail operate(GetUserCmd cmd);
}
