package io.dough.api.useCases.user.profile.updatePassword.application;

import io.dough.api.useCases.user.profile.updatePassword.domain.Password;
import java.util.UUID;

public interface LoadPassword {
  Password operate(UUID userId);
}
