package io.dough.api.useCases.user.mgmt.searchUsers.application.model;

import java.util.List;

import io.dough.api.useCases.shared.application.service.model.PageInfo;

public record SearchUsers(List<SearchedUser> users, PageInfo pageInfo) {}
