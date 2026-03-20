package io.dough.api.useCases.user.mgmt.searchUsers.application.port.in;

import java.util.List;

import io.dough.api.useCases.shared.application.service.pageable.PageInfo;

public record SearchUsers(List<SearchedUser> users, PageInfo pageInfo) {}
