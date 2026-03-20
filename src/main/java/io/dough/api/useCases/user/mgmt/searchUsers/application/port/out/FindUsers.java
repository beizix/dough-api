package io.dough.api.useCases.user.mgmt.searchUsers.application.port.out;

import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchUsers;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchUsersCmd;

public interface FindUsers {
  SearchUsers operate(SearchUsersCmd cmd);
}
