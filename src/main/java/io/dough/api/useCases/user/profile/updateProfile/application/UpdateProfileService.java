package io.dough.api.useCases.user.profile.updateProfile.application;

import io.dough.api.useCases.user.profile.updateProfile.application.model.ProfileUpdated;
import io.dough.api.useCases.user.profile.updateProfile.application.model.UpdateProfileCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateProfileService implements UpdateProfileUseCase {

  private final UpdateProfile updateProfile;

  @Override
  public ProfileUpdated operate(UpdateProfileCmd cmd) {
    return updateProfile.operate(cmd);
  }
}
