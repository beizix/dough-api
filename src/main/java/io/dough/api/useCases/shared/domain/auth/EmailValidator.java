package io.dough.api.useCases.shared.domain.auth;

import java.util.regex.Pattern;

public class EmailValidator {

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

  public static void validate(String email) {
    if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
      throw new IllegalArgumentException("exception.auth.invalid_email_format");
    }
  }

  public static void validateIfPresent(String email) {
    if (email != null) {
      validate(email);
    }
  }
}
