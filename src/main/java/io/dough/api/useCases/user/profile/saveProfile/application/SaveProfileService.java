package io.dough.api.useCases.user.profile.saveProfile.application;

import io.dough.api.useCases.user.profile.saveProfile.domain.SaveProfileCmd;
import io.dough.api.useCases.user.profile.saveProfile.domain.SavedProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveProfileService implements SaveProfileUseCase {

  private final UpdateProfile updateProfile;

  @Override
  public SavedProfile operate(SaveProfileCmd cmd) {
    return updateProfile.operate(cmd);
  }
}
