package io.dough.api.useCases.user.maintenance.createManager.domain;

import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.regex.Pattern;

public record CreateManagerCmd(String email, String displayName, String password, Role role) {

  private static final Pattern PASSWORD_PATTERN =
      Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W]{8,}$");

  public CreateManagerCmd {
    if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
      throw new IllegalArgumentException("exception.auth.invalid_password_format");
    }
  }

  public CreateManagerCmd(String email, String displayName, String password) {
    this(email, displayName, password, Role.MANAGER);
  }
}
