package io.dough.api.useCases.user.profile.saveProfileImg.application;

import io.dough.api.useCases.user.profile.saveProfileImg.application.domain.model.SaveProfileImgCmd;
import io.dough.api.useCases.user.profile.saveProfileImg.application.domain.model.SavedProfileImg;
import java.util.Optional;

public interface SaveProfileImgUseCase {
  Optional<SavedProfileImg> operate(SaveProfileImgCmd cmd);
}
