package io.dough.api.useCases.user.maintenance.removeManager.domain;

import java.time.LocalDateTime;

public record ManagerRemoved(boolean removed, LocalDateTime deletedAt) {}
