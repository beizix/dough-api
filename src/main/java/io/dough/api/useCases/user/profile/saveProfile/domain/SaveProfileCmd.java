package io.dough.api.useCases.user.profile.saveProfile.domain;

import io.dough.api.useCases.shared.domain.auth.DisplayNameValidator;
import io.dough.api.useCases.shared.domain.auth.EmailValidator;
import java.util.UUID;

public record SaveProfileCmd(UUID loginUserId, String email, String displayName) {
  public SaveProfileCmd {
    EmailValidator.validate(email);
    DisplayNameValidator.validate(displayName);
  }
}
