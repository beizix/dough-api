package io.dough.api.useCases.user.maintenance.updateManager.application.model;

import io.dough.api.useCases.shared.domain.auth.DisplayNameValidator;
import io.dough.api.useCases.shared.domain.auth.EmailValidator;
import io.dough.api.useCases.shared.domain.auth.PasswordValidator;

public record UpdateManagerCmd(String email, String displayName, String password) {

  public UpdateManagerCmd {
    EmailValidator.validateIfPresent(email);
    DisplayNameValidator.validateIfPresent(displayName);
    PasswordValidator.validateIfPresent(password);
  }
}
