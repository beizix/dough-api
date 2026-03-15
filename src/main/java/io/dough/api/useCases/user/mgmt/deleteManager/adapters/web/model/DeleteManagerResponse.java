package io.dough.api.useCases.user.mgmt.deleteManager.adapters.web.model;

import io.dough.api.useCases.user.mgmt.deleteManager.application.model.ManagerDeleted;
import java.time.LocalDateTime;

public record DeleteManagerResponse(boolean deleted, LocalDateTime deletedAt) {
  public static DeleteManagerResponse from(ManagerDeleted domain) {
    return new DeleteManagerResponse(domain.deleted(), domain.deletedAt());
  }
}
