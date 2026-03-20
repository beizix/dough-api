package io.dough.api.useCases.user.mgmt.updateManager.application.port.in;

import io.dough.api.useCases.shared.application.service.validator.DisplayNameValidator;
import io.dough.api.useCases.shared.application.service.validator.EmailValidator;
import io.dough.api.useCases.shared.application.service.validator.PasswordValidator;

public record UpdateManagerCmd(String email, String displayName, String password) {

  public UpdateManagerCmd {
    EmailValidator.validateIfPresent(email);
    DisplayNameValidator.validateIfPresent(displayName);
    PasswordValidator.validateIfPresent(password);
  }
}
