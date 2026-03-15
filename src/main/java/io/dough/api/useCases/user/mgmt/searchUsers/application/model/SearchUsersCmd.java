package io.dough.api.useCases.user.mgmt.searchUsers.application.model;

import io.dough.api.useCases.shared.domain.auth.Role;

public record SearchUsersCmd(
    String email, String displayName, Role role, int page, int size, String sort) {}
