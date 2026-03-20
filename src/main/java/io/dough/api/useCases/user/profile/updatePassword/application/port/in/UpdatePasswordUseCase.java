package io.dough.api.useCases.user.profile.updatePassword.application.port.in;

public interface UpdatePasswordUseCase {
  void operate(UpdatePasswordCmd command);
}
