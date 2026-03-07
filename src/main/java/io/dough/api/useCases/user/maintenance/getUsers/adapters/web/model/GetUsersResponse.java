package io.dough.api.useCases.user.maintenance.getUsers.adapters.web.model;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.getUsers.domain.GetUsers;
import io.dough.api.useCases.user.maintenance.getUsers.domain.PageInfo;
import java.util.List;
import java.util.UUID;

public record GetUsersResponse(List<UserItemResponse> users, PageInfo pageInfo) {

  public static GetUsersResponse from(GetUsers domain) {
    return new GetUsersResponse(
        domain.users().stream().map(UserItemResponse::from).toList(), domain.pageInfo());
  }

  public record UserItemResponse(UUID id, String email, String displayName, Role role) {
    public static UserItemResponse from(
        io.dough.api.useCases.user.maintenance.getUsers.domain.UserForList user) {
      return new UserItemResponse(user.id(), user.email(), user.displayName(), user.role());
    }
  }
}
