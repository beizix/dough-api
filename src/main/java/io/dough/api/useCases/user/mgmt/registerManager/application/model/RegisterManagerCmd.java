package io.dough.api.useCases.user.mgmt.registerManager.application.model;

import io.dough.api.useCases.shared.domain.auth.DisplayNameValidator;
import io.dough.api.useCases.shared.domain.auth.EmailValidator;
import io.dough.api.useCases.shared.domain.auth.PasswordValidator;
import io.dough.api.useCases.shared.domain.auth.Role;

public record RegisterManagerCmd(String email, String displayName, String password, Role role) {

  public RegisterManagerCmd {
    EmailValidator.validate(email);
    DisplayNameValidator.validate(displayName);
    PasswordValidator.validate(password);
  }

  public RegisterManagerCmd(String email, String displayName, String password) {
    this(email, displayName, password, Role.MANAGER);
  }
}
