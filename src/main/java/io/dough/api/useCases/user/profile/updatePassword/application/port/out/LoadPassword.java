package io.dough.api.useCases.user.profile.updatePassword.application.port.out;

import java.util.UUID;

public interface LoadPassword {
  Password operate(UUID userId);
}
