package io.dough.api.useCases.user.mgmt.searchUsers.application;

import io.dough.api.useCases.user.mgmt.searchUsers.application.model.SearchUsers;
import io.dough.api.useCases.user.mgmt.searchUsers.application.model.SearchUsersCmd;
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
