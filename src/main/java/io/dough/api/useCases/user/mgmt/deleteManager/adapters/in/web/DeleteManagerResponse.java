package io.dough.api.useCases.user.mgmt.deleteManager.adapters.in.web;

import io.dough.api.useCases.user.mgmt.deleteManager.application.port.in.ManagerDeleted;
import java.time.LocalDateTime;

public record DeleteManagerResponse(boolean deleted, LocalDateTime deletedAt) {
  public static DeleteManagerResponse from(ManagerDeleted domain) {
    return new DeleteManagerResponse(domain.deleted(), domain.deletedAt());
  }
}
