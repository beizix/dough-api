package io.dough.api.useCases.user.registerUser.application.port.in;

public interface RegisterUserUseCase {
  RegisteredToken operate(RegisterUserCmd command);
}
