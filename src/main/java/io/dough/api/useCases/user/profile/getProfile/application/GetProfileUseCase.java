package io.dough.api.useCases.user.profile.getProfile.application;

import io.dough.api.useCases.user.profile.getProfile.domain.GetProfileCmd;
import io.dough.api.useCases.user.profile.getProfile.domain.Profile;

public interface GetProfileUseCase {
  Profile operate(GetProfileCmd cmd);
}
