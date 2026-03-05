package io.dough.api.useCases.user.profile.updatePassword.application;

import io.dough.api.useCases.user.profile.updatePassword.domain.UpdatePasswordCmd;

public interface UpdatePasswordUseCase {
  void operate(UpdatePasswordCmd command);
}
