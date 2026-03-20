package io.dough.api.useCases.user.profile.updateProfile.application.port.in;

public interface UpdateProfileUseCase {
  ProfileUpdated operate(UpdateProfileCmd cmd);
}
