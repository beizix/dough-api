package io.dough.api.useCases.user.maintenance.updateManager.domain;

import io.dough.api.useCases.shared.domain.auth.PasswordValidator;

public record UpdateManagerCmd(String email, String displayName, String password) {

  public UpdateManagerCmd {
    PasswordValidator.validateIfPresent(password);
  }
}
