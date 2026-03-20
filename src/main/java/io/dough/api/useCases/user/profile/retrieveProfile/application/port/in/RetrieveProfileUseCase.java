package io.dough.api.useCases.user.profile.retrieveProfile.application.port.in;

public interface RetrieveProfileUseCase {
  RetrieveProfile operate(RetrieveProfileCmd cmd);
}
