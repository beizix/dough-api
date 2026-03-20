package io.dough.api.useCases.user.profile.updateProfile.application.service;

import io.dough.api.useCases.user.profile.updateProfile.application.port.in.ProfileUpdated;
import io.dough.api.useCases.user.profile.updateProfile.application.port.in.UpdateProfileCmd;
import io.dough.api.useCases.user.profile.updateProfile.application.port.in.UpdateProfileUseCase;
import io.dough.api.useCases.user.profile.updateProfile.application.port.out.UpdateProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class UpdateProfileService implements UpdateProfileUseCase {

  private final UpdateProfile updateProfile;

  @Override
  public ProfileUpdated operate(UpdateProfileCmd cmd) {
    return updateProfile.operate(cmd);
  }
}
