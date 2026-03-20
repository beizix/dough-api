package io.dough.api.useCases.user.mgmt.searchUsers.application.port.in;

import io.dough.api.useCases.shared.application.service.model.PageInfo;
import java.util.List;

public record SearchUsers(List<SearchedUser> users, PageInfo pageInfo) {}
