package io.dough.api.useCases.user.maintenance.removeManager.adapters.web.model;

import io.dough.api.useCases.user.maintenance.removeManager.domain.ManagerRemoved;
import java.time.LocalDateTime;

public record RemoveManagerResponse(boolean removed, LocalDateTime deletedAt) {
  public static RemoveManagerResponse from(ManagerRemoved domain) {
    return new RemoveManagerResponse(domain.removed(), domain.deletedAt());
  }
}
