package io.dough.api.useCases.user.profile.updatePassword.application.domain.model;

import io.dough.api.common.application.utils.MessageUtils;
import java.util.UUID;

public record UpdatePasswordCmd(
    UUID userId,
    String currentPassword,
    String newPassword,
    String newPasswordConfirm
) {

  public UpdatePasswordCmd {
    if (!newPassword.equals(newPasswordConfirm)) {
      throw new IllegalArgumentException(MessageUtils.get("error.password.mismatch"));
    }
  }
}
