package io.dough.api.useCases.user.mgmt.searchUsers.application.port.in;

public interface SearchUsersUseCase {
  SearchUsers operate(SearchUsersCmd cmd);
}
