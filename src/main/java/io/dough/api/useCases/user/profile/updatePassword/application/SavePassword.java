package io.dough.api.useCases.user.profile.updatePassword.application;

import io.dough.api.useCases.user.profile.updatePassword.application.model.Password;

public interface SavePassword {
  void operate(Password password);
}
