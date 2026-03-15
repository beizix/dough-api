package io.dough.api.useCases.user.profile.updatePassword.application;

import io.dough.api.useCases.user.profile.updatePassword.domain.Password;

public interface SavePassword {
  void operate(Password password);
}
