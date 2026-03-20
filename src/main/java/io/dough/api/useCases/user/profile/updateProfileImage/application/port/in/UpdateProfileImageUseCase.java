package io.dough.api.useCases.user.profile.updateProfileImage.application.port.in;

import java.util.Optional;

public interface UpdateProfileImageUseCase {
  Optional<ProfileImageUpdated> operate(UpdateProfileImageCmd cmd);
}
