package io.dough.api.useCases.user.maintenance.getUsers.domain;

import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.UUID;

public record UserForList(UUID id, String email, String displayName, Role role) {}
