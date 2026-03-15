package io.dough.api.useCases.user.mgmt.deleteManager.application.model;

import java.util.UUID;

public record DeleteManagerCmd(UUID id, String deletedBy) {}
