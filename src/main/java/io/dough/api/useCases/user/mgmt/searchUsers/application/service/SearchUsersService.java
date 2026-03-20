package io.dough.api.useCases.user.mgmt.searchUsers.application.service;

import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchUsers;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchUsersCmd;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchUsersUseCase;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.out.FindUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchUsersService implements SearchUsersUseCase {

  private final FindUsers findUsers;

  @Override
  public SearchUsers operate(SearchUsersCmd cmd) {
    return findUsers.operate(cmd);
  }
}
