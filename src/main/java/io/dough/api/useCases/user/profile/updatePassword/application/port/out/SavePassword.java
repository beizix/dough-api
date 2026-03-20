package io.dough.api.useCases.user.profile.updatePassword.application.port.out;

public interface SavePassword {
  void operate(Password password);
}
