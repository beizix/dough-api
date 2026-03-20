package io.dough.api.useCases.user.mgmt.searchUsers.adapters.in.web;

import io.dough.api.useCases.shared.application.service.pageable.PageInfo;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchUsers;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchedUser;
import java.util.List;
import java.util.UUID;

public record GetUsersResponse(List<UserItemResponse> users, PageInfo pageInfo) {

  public static GetUsersResponse from(SearchUsers domain) {
    return new GetUsersResponse(
        domain.users().stream().map(UserItemResponse::from).toList(), domain.pageInfo());
  }

  public record UserItemResponse(UUID id, String email, String displayName, Role role) {
    public static UserItemResponse from(SearchedUser user) {
      return new UserItemResponse(user.id(), user.email(), user.displayName(), user.role());
    }
  }
}
