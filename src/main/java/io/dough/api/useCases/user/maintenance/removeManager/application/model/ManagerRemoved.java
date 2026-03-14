package io.dough.api.useCases.user.maintenance.removeManager.application.model;

import java.time.LocalDateTime;

public record ManagerRemoved(boolean removed, LocalDateTime deletedAt) {}
