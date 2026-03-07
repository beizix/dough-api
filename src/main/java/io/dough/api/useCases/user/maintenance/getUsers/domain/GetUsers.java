package io.dough.api.useCases.user.maintenance.getUsers.domain;

import java.util.List;

public record GetUsers(List<UserForList> users, PageInfo pageInfo) {}
