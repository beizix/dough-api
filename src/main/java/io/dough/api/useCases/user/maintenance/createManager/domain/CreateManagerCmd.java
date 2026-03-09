package io.dough.api.useCases.user.maintenance.createManager.domain;

import io.dough.api.useCases.shared.domain.auth.DisplayNameValidator;
import io.dough.api.useCases.shared.domain.auth.EmailValidator;
import io.dough.api.useCases.shared.domain.auth.PasswordValidator;
import io.dough.api.useCases.shared.domain.auth.Role;

public record CreateManagerCmd(String email, String displayName, String password, Role role) {

  public CreateManagerCmd {
    EmailValidator.validate(email);
    DisplayNameValidator.validate(displayName);
    PasswordValidator.validate(password);
  }

  public CreateManagerCmd(String email, String displayName, String password) {
    this(email, displayName, password, Role.MANAGER);
  }
}
