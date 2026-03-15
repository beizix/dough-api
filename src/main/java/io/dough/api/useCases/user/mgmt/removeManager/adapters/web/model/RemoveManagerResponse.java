package io.dough.api.useCases.user.mgmt.removeManager.adapters.web.model;

import io.dough.api.useCases.user.mgmt.removeManager.application.model.ManagerRemoved;
import java.time.LocalDateTime;

public record RemoveManagerResponse(boolean removed, LocalDateTime deletedAt) {
  public static RemoveManagerResponse from(ManagerRemoved domain) {
    return new RemoveManagerResponse(domain.removed(), domain.deletedAt());
  }
}
