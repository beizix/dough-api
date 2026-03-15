package io.dough.api.useCases.user.profile.updateProfileImage.application;

import io.dough.api.useCases.user.profile.updateProfileImage.application.model.ProfileImageUpdated;
import io.dough.api.useCases.user.profile.updateProfileImage.application.model.UpdateProfileImageCmd;
import java.util.Optional;

public interface UpdateProfileImageUseCase {
  Optional<ProfileImageUpdated> operate(UpdateProfileImageCmd cmd);
}
