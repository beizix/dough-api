package io.dough.api.useCases.user.mgmt.searchUsers.application.port.in;

import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchUsers;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchUsersCmd;

public interface SearchUsersUseCase {
  SearchUsers operate(SearchUsersCmd cmd);
}
