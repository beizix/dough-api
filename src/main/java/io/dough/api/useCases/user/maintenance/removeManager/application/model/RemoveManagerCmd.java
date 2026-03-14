package io.dough.api.useCases.user.maintenance.removeManager.application.model;

import java.util.UUID;

public record RemoveManagerCmd(UUID id, String removedBy) {}
