package io.dough.api.useCases.user.profile.updatePassword.application;

import io.dough.api.useCases.user.profile.updatePassword.domain.UpdatedPassword;

public interface SaveUser {
  void operate(UpdatedPassword updatedPassword);
}
