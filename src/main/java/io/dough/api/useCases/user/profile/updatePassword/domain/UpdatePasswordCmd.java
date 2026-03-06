package io.dough.api.useCases.user.profile.updatePassword.domain;

import java.util.UUID;

public record UpdatePasswordCmd(
    UUID userId,
    String currentPassword,
    String newPassword,
    String newPasswordConfirm
) {

  public UpdatePasswordCmd {
    if (!newPassword.equals(newPasswordConfirm)) {
      throw new IllegalArgumentException("error.password.mismatch");
    }
  }
}
