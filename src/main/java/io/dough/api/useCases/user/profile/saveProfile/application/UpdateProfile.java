package io.dough.api.useCases.user.profile.saveProfile.application;

import io.dough.api.useCases.user.profile.saveProfile.application.model.SaveProfileCmd;
import io.dough.api.useCases.user.profile.saveProfile.application.model.SavedProfile;

public interface UpdateProfile {
  SavedProfile operate(SaveProfileCmd cmd);
}
