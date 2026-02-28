package io.dough.api.useCases.user.profile.getProfile.application;

import io.dough.api.useCases.user.profile.getProfile.application.domain.model.GetProfileCmd;
import io.dough.api.useCases.user.profile.getProfile.application.domain.model.Profile;

public interface GetProfileUseCase {
  Profile operate(GetProfileCmd cmd);
}
