package io.dough.api.useCases.user.mgmt.updateManager.application.model;

import io.dough.api.useCases.shared.application.validator.DisplayNameValidator;
import io.dough.api.useCases.shared.application.validator.EmailValidator;
import io.dough.api.useCases.shared.application.validator.PasswordValidator;

public record UpdateManagerCmd(String email, String displayName, String password) {

  public UpdateManagerCmd {
    EmailValidator.validateIfPresent(email);
    DisplayNameValidator.validateIfPresent(displayName);
    PasswordValidator.validateIfPresent(password);
  }
}
