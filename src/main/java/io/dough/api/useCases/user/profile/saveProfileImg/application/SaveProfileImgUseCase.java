package io.dough.api.useCases.user.profile.saveProfileImg.application;

import io.dough.api.useCases.user.profile.saveProfileImg.domain.SaveProfileImgCmd;
import io.dough.api.useCases.user.profile.saveProfileImg.domain.SavedProfileImg;
import java.util.Optional;

public interface SaveProfileImgUseCase {
  Optional<SavedProfileImg> operate(SaveProfileImgCmd cmd);
}
