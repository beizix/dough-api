package io.dough.api.useCases.user.mgmt.deleteManager.application.port.in;

import java.util.UUID;

public record DeleteManagerCmd(UUID id, String deletedBy) {}
