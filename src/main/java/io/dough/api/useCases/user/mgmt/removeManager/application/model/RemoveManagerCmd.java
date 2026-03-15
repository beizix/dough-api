package io.dough.api.useCases.user.mgmt.removeManager.application.model;

import java.util.UUID;

public record RemoveManagerCmd(UUID id, String removedBy) {}
