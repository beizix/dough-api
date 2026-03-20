package io.dough.api.useCases.user.profile.updateProfile.application.port.out;

import io.dough.api.useCases.user.profile.updateProfile.application.port.in.ProfileUpdated;
import io.dough.api.useCases.user.profile.updateProfile.application.port.in.UpdateProfileCmd;

public interface UpdateProfile {
  ProfileUpdated operate(UpdateProfileCmd cmd);
}
