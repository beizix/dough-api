package io.dough.api.useCases.user.profile.updatePassword.application;

import io.dough.api.useCases.user.profile.updatePassword.domain.UpdatePassword;
import java.util.UUID;

public interface GetUser {
  UpdatePassword operate(UUID userId);
}
