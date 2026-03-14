package io.dough.api.useCases.user.maintenance.getUsers.application.model;

import io.dough.api.useCases.shared.domain.auth.Role;

public record GetUsersCmd(
    String email, String displayName, Role role, int page, int size, String sort) {}
