package io.dough.api.useCases.user.profile.updatePassword.application.model;

import io.dough.api.useCases.shared.domain.auth.PasswordValidator;
import java.util.UUID;

public record UpdatePasswordCmd(
    UUID userId, String currentPassword, String newPassword, String newPasswordConfirm) {

  public UpdatePasswordCmd {
    if (!newPassword.equals(newPasswordConfirm)) {
      throw new IllegalArgumentException("error.password.mismatch");
    }
    PasswordValidator.validate(newPassword);
  }
}
