package io.dough.api.useCases.user.maintenance.updateManager.domain;

import java.util.regex.Pattern;

public record UpdateManagerCmd(String email, String displayName, String password) {

  private static final Pattern PASSWORD_PATTERN =
      Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W]{8,}$");

  public UpdateManagerCmd {
    if (password != null && !PASSWORD_PATTERN.matcher(password).matches()) {
      throw new IllegalArgumentException("exception.auth.invalid_password_format");
    }
  }
}
