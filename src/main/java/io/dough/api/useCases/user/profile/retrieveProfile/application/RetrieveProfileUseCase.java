package io.dough.api.useCases.user.profile.retrieveProfile.application;

import io.dough.api.useCases.user.profile.retrieveProfile.application.model.RetrieveProfile;
import io.dough.api.useCases.user.profile.retrieveProfile.application.model.RetrieveProfileCmd;

public interface RetrieveProfileUseCase {
  RetrieveProfile operate(RetrieveProfileCmd cmd);
}
