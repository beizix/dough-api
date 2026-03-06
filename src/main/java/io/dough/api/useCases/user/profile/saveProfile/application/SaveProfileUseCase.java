package io.dough.api.useCases.user.profile.saveProfile.application;

import io.dough.api.useCases.user.profile.saveProfile.domain.SaveProfileCmd;
import io.dough.api.useCases.user.profile.saveProfile.domain.SavedProfile;

public interface SaveProfileUseCase {
  SavedProfile operate(SaveProfileCmd cmd);
}
