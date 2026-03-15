package io.dough.api.useCases.user.mgmt.searchUsers.application;

import io.dough.api.useCases.user.mgmt.searchUsers.application.model.SearchUsers;
import io.dough.api.useCases.user.mgmt.searchUsers.application.model.SearchUsersCmd;

public interface FindUsers {
  SearchUsers operate(SearchUsersCmd cmd);
}
