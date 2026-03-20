package io.dough.api.useCases.user.mgmt.deleteManager.application.port.in;

import java.time.LocalDateTime;

public record ManagerDeleted(boolean deleted, LocalDateTime deletedAt) {}
