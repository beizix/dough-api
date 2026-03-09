package io.dough.api.useCases.shared.domain.auth;

public class DisplayNameValidator {

  public static void validate(String displayName) {
    if (displayName == null || displayName.length() < 2 || displayName.length() > 20) {
      throw new IllegalArgumentException("exception.auth.invalid_display_name_format");
    }
  }

  public static void validateIfPresent(String displayName) {
    if (displayName != null) {
      validate(displayName);
    }
  }
}
