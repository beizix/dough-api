package io.dough.api.useCases.user.profile.getProfile.application;

import io.dough.api.useCases.user.profile.getProfile.application.model.GetProfileCmd;
import io.dough.api.useCases.user.profile.getProfile.application.model.UserProfile;

public interface GetProfileUseCase {
  UserProfile operate(GetProfileCmd cmd);
}
