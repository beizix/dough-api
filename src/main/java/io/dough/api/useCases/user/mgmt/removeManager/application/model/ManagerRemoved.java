package io.dough.api.useCases.user.mgmt.removeManager.application.model;

import java.time.LocalDateTime;

public record ManagerRemoved(boolean removed, LocalDateTime deletedAt) {}
