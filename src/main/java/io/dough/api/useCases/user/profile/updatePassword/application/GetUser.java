package io.dough.api.useCases.user.profile.updatePassword.application;

import io.dough.api.useCases.user.profile.updatePassword.domain.model.UpdatePassword;
import java.util.UUID;

public interface GetUser {
  UpdatePassword operate(UUID userId);
}
