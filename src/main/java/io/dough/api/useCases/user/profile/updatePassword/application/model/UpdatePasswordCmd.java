package io.dough.api.useCases.user.profile.updatePassword.application.model;

import java.util.UUID;

import io.dough.api.useCases.shared.application.service.validator.PasswordValidator;

public record UpdatePasswordCmd(
    UUID userId, String currentPassword, String newPassword, String newPasswordConfirm) {

  public UpdatePasswordCmd {
    if (!newPassword.equals(newPasswordConfirm)) {
      throw new IllegalArgumentException("error.password.mismatch");
    }
    PasswordValidator.validate(newPassword);
  }
}
