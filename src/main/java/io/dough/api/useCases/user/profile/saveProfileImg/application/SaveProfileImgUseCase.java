package io.dough.api.useCases.user.profile.saveProfileImg.application;

import io.dough.api.useCases.user.profile.saveProfileImg.application.model.SaveProfileImgCmd;
import io.dough.api.useCases.user.profile.saveProfileImg.application.model.SavedProfileImg;
import java.util.Optional;

public interface SaveProfileImgUseCase {
  Optional<SavedProfileImg> operate(SaveProfileImgCmd cmd);
}
