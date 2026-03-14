package io.dough.api.useCases.user.maintenance.getUsers.application.model;

import java.util.List;

public record GetUsers(List<UserForList> users, PageInfo pageInfo) {}
