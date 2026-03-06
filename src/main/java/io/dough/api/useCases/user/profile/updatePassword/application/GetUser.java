package io.dough.api.useCases.user.profile.updatePassword.application;

import io.dough.api.useCases.user.profile.updatePassword.domain.UpdatedPassword;
import java.util.UUID;

public interface GetUser {
  UpdatedPassword operate(UUID userId);
}
