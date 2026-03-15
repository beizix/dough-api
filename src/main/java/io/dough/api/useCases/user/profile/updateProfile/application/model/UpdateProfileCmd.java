package io.dough.api.useCases.user.profile.updateProfile.application.model;

import io.dough.api.useCases.shared.domain.auth.DisplayNameValidator;
import io.dough.api.useCases.shared.domain.auth.EmailValidator;
import java.util.UUID;

public record UpdateProfileCmd(UUID loginUserId, String email, String displayName) {
  public UpdateProfileCmd {
    EmailValidator.validate(email);
    DisplayNameValidator.validate(displayName);
  }
}
