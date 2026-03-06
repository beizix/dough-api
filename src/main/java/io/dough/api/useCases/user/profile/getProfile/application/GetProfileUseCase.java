package io.dough.api.useCases.user.profile.getProfile.application;

import io.dough.api.useCases.user.profile.getProfile.domain.GetProfileCmd;
import io.dough.api.useCases.user.profile.getProfile.domain.UserProfile;

public interface GetProfileUseCase {
  UserProfile operate(GetProfileCmd cmd);
}
