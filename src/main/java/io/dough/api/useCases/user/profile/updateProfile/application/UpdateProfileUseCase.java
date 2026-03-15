package io.dough.api.useCases.user.profile.updateProfile.application;

import io.dough.api.useCases.user.profile.updateProfile.application.model.ProfileUpdated;
import io.dough.api.useCases.user.profile.updateProfile.application.model.UpdateProfileCmd;

public interface UpdateProfileUseCase {
  ProfileUpdated operate(UpdateProfileCmd cmd);
}
