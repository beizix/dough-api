package io.dough.api.useCases.user.mgmt.searchUsers.application.port.in;

import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.UUID;

public record SearchedUser(UUID id, String email, String displayName, Role role) {}
