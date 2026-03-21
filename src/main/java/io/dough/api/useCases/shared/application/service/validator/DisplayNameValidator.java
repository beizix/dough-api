package io.dough.api.useCases.shared.application.service.validator;

public class DisplayNameValidator {

  public static void validate(String displayName) {
    if (displayName == null || displayName.length() < 1 || displayName.length() >= 20) {
      throw new IllegalArgumentException("exception.auth.invalid_display_name_format");
    }
  }

  public static void validateIfPresent(String displayName) {
    if (displayName != null) {
      validate(displayName);
    }
  }
}
