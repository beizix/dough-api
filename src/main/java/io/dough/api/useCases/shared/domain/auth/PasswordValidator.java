package io.dough.api.useCases.shared.domain.auth;

import java.util.regex.Pattern;

public class PasswordValidator {

  private static final Pattern PASSWORD_PATTERN =
      Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W]{8,}$");

  public static void validate(String password) {
    if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
      throw new IllegalArgumentException("exception.auth.invalid_password_format");
    }
  }

  public static void validateIfPresent(String password) {
    if (password != null) {
      validate(password);
    }
  }
}
