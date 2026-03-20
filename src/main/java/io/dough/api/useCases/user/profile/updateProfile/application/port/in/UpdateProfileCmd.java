package io.dough.api.useCases.user.profile.updateProfile.application.port.in;

import io.dough.api.useCases.shared.application.service.validator.DisplayNameValidator;
import io.dough.api.useCases.shared.application.service.validator.EmailValidator;
import java.util.UUID;

public record UpdateProfileCmd(UUID loginUserId, String email, String displayName) {
  public UpdateProfileCmd {
    EmailValidator.validate(email);
    DisplayNameValidator.validate(displayName);
  }
}
